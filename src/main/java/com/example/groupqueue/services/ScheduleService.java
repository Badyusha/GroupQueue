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
		switch(dayOfWeek) {
			case DayOfWeek.MONDAY : {
				schedule.setMonday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.TUESDAY : {
				schedule.setTuesday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.WEDNESDAY : {
				schedule.setWednesday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.THURSDAY : {
				schedule.setThursday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.FRIDAY : {
				schedule.setFriday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.SATURDAY : {
				schedule.setSaturday(dayOfWeekScheduled);
				break;
			}
			case DayOfWeek.SUNDAY : {
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
		dayOfWeekScheduled.setDate(lessonService.getLessonDate(dayOfWeek, lessonList.getFirst().getWeekType()));

		List<Lesson> dayOfWeekLessonList = new ArrayList<>();
		for(Lesson lesson : lessonList) {
			if(lesson.getDayOfWeek() == dayOfWeek) {
				dayOfWeekLessonList.add(lesson);
			}
		}

		dayOfWeekScheduled.setLessons(dayOfWeekLessonList);
		return dayOfWeekScheduled;
	}

	private List<Lesson> createLessonList(long userId, long groupId) {
		List<Lesson> lessonList = new ArrayList<>();
		for(Object[] lesson : lessonService.getScheduleInfoByUserIdGroupId(userId, groupId)) {
			Long lessonId = (Long) lesson[0];
			String subjectName = (String) lesson[1];
			String subjectFullName = (String) lesson[2];
			DayOfWeek dayOfWeek = (DayOfWeek) lesson[3];
			SubgroupType subgroupType = (SubgroupType) lesson[4];
			LocalDate date = (LocalDate) lesson[5];
			WeekType weekType = (WeekType) lesson[6];
			LocalTime startTime = (LocalTime) lesson[7];
			Integer numberInQueue = (Integer) lesson[8];
			Long queueId = (Long) lesson[9];
			Boolean isRegisteredInQueue = (Boolean) lesson[10];

			lessonList.add(new Lesson(lessonId,
										subjectName,
										subjectFullName,
										subgroupType,
										startTime,
										isRegisteredInQueue,
										numberInQueue,
										queueId,
										dayOfWeek,
										date,
										weekType));
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
