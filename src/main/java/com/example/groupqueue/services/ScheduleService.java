package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.ScheduleException;
import com.example.groupqueue.models.dto.DayOfWeekScheduled;
import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.ScheduleRepository;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private static final String GROUP_SCHEDULE_URL = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";

	private final ScheduleRepository scheduleRepository;
	private final GroupRepository groupRepository;
	private final LessonService lessonService;

	public Schedule getDayOfWeekScheduleList(HttpServletRequest request) {
		long userId = CookieUtils.getUserId(request);
		long groupId = CookieUtils.getGroupId(request);

		List<Lesson> lessonList = createLessonList(userId, groupId);

		if(lessonList.isEmpty()) {
			return null;
		}

		Schedule schedule = new Schedule();
		for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
			DayOfWeekScheduled dayOfWeekScheduled = fillDayOfWeekSchedule(lessonList, dayOfWeek);
			addDayOfWeekScheduledIntoSchedule(schedule, dayOfWeekScheduled, dayOfWeek);
		}

		return schedule;
	}

	private void addDayOfWeekScheduledIntoSchedule(Schedule schedule,
												   DayOfWeekScheduled dayOfWeekScheduled,
												   DayOfWeek dayOfWeek) {
		switch(dayOfWeek.day) {
			case "MONDAY" : {
				schedule.setMonday(dayOfWeekScheduled);
				break;
			}
			case "TUESDAY" : {
				schedule.setTuesday(dayOfWeekScheduled);
				break;
			}
			case "WEDNESDAY" : {
				schedule.setWednesday(dayOfWeekScheduled);
				break;
			}
			case "THURSDAY" : {
				schedule.setThursday(dayOfWeekScheduled);
				break;
			}
			case "FRIDAY" : {
				schedule.setFriday(dayOfWeekScheduled);
				break;
			}
			case "SATURDAY" : {
				schedule.setSaturday(dayOfWeekScheduled);
				break;
			}
			case "SUNDAY" : {
				schedule.setSunday(dayOfWeekScheduled);
				break;
			}
		}
	}

	private DayOfWeekScheduled fillDayOfWeekSchedule(List<Lesson> lessonList, DayOfWeek dayOfWeek) {
		if(lessonList.isEmpty()) {
			return null;
		}

		DayOfWeekScheduled dayOfWeekScheduled = new DayOfWeekScheduled();
		List<Lesson> dayOfWeekLessonList = new ArrayList<>();
		for(Lesson lesson : lessonList) {
			if(lesson.getDayOfWeek() == dayOfWeek) {
				dayOfWeekLessonList.add(lesson);
			}
		}

		if(dayOfWeekLessonList.isEmpty()) {
			return null;
		}

		dayOfWeekScheduled.setDate(lessonList.getFirst().getDate());
		dayOfWeekScheduled.setLessons(dayOfWeekLessonList);
		return dayOfWeekScheduled;
	}

	private List<Lesson> createLessonList(long userId, long groupId) {
		List<Lesson> lessonList = new ArrayList<>();
		for(Object[] row : lessonService.getScheduleInfoByUserIdGroupId(userId, groupId)) {
			Long lessonId = (Long) row[0];
			String subjectName = (String) row[1];
			String subjectFullName = (String) row[2];
			DayOfWeek dayOfWeek = (DayOfWeek) row[3];
			SubgroupType subgroupType = (SubgroupType) row[4];
			LocalDate date = (LocalDate) row[5];
			LocalTime startTime = (LocalTime) row[6];
			Integer numberInQueue = (Integer) row[7];
			Boolean isRegisteredInQueue = (Boolean) row[8];

			Lesson lesson = new Lesson(lessonId,
					subjectName,
					subjectFullName,
					subgroupType,
					startTime,
					isRegisteredInQueue,
					numberInQueue,
					dayOfWeek,
					date);
			lessonList.add(lesson);
		}
		return lessonList;
	}



	//	code below add records into schedule and lesson tables ===
	public void addRecordsForNewGroupByGroupNumber(int groupNumber) {
		int scheduleWeek = WeekType.getCurrentWeekViaApi();
		if(isTimeToGenerateNextWeekSchedule()) {
			scheduleWeek = WeekType.getNextWeekNumber(scheduleWeek);
		}

		addScheduleByGroupNumber(groupNumber, scheduleWeek);
		List<ScheduleEntity> scheduleEntityList =
				getScheduleEntityByGroupNumberWeekType(groupNumber, WeekType.getWeekTypeByNumber(scheduleWeek));
		lessonService.addLessonByScheduleList(scheduleWeek, scheduleEntityList);
	}

	public List<ScheduleEntity> getScheduleEntityByGroupNumberWeekType(int groupNumber, WeekType week) {
		return scheduleRepository.getScheduleEntityByGroupNumberWeekType(groupNumber, week);
	}

	private void addScheduleByGroupNumber(int groupNumber, int week) {
		String groupScheduleJson = getGroupScheduleViaApi(GROUP_SCHEDULE_URL, groupNumber);
		JSONObject scheduleData = new JSONObject(groupScheduleJson);
		JSONObject schedules = scheduleData.getJSONObject("schedules");
		for (String dayOfWeek : schedules.keySet()) {
			JSONArray daySchedule = schedules.getJSONArray(dayOfWeek);
			for (int i = 0; i < daySchedule.length(); i++) {
				JSONObject lesson = daySchedule.getJSONObject(i);

				// if lesson IS NOT lab
				if(!lesson.getString("lessonTypeAbbrev").equals("ЛР") ||
					!hasValue(lesson.getJSONArray("weekNumber"), week)) {
					continue;
				}

				saveSchedule(lesson, groupNumber, week, dayOfWeek);
			}
		}
	}

	private void saveSchedule(JSONObject lesson, int groupNumber, int week, String dayOfWeek) {
		String subject = lesson.getString("subject");
		String subjectFullName = lesson.getString("subjectFullName");
		SubgroupType subgroupType = SubgroupType.getSubgroupType(lesson.getInt("numSubgroup"));
		LocalTime startTime = LocalTime.parse(lesson.getString("startLessonTime"));
		Long groupId = groupRepository.getGroupIdByNumber(groupNumber);
		WeekType weekType = WeekType.getWeekTypeByNumber(week);
		DayOfWeek day = DayOfWeek.getDayOfWeekByName(dayOfWeek);

		scheduleRepository.save(new ScheduleEntity(
				subject,
				subjectFullName,
				subgroupType,
				startTime,
				groupId,
				weekType,
				day
		));
	}

	private boolean isTimeToGenerateNextWeekSchedule() {
		LocalTime generateNewScheduleTime = WeekType.TIME_TO_GENERATE_NEW_SCHEDULE;
		boolean isTimeToGenerateNewSchedule = LocalTime.now().isBefore(generateNewScheduleTime);
		boolean isTodaySunday = DayOfWeek.getDayOfWeekFromCalendar().equals(DayOfWeek.SUNDAY);

		return isTimeToGenerateNewSchedule && isTodaySunday;
	}

	private static String getGroupScheduleViaApi(String str, Integer groupNumber) {
		String response;
		try {
			URL url = new URI(str + groupNumber.toString()).toURL();
			response = IOUtils.toString(url, StandardCharsets.UTF_8);
			if(response == null) {
				throw new ScheduleException("response is null in makeGetGroupScheduleRequest()");
			}
		} catch(URISyntaxException | IOException e) {
			throw new ScheduleException("URISyntaxException or IOException in makeGetGroupScheduleRequest()");
		}
		return response;
	}

	private static boolean hasValue(JSONArray array, int value) {
		for(int i = 0; i < array.length(); ++i) {
			if(array.getInt(i) == value) {
				return true;
			}
		}
		return false;
	}
	// ===
}
