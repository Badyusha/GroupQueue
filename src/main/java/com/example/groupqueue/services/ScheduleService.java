package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private static final String groupScheduleUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";
	private static final String currentWeekUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";

	private final ScheduleRepository scheduleRepository;
	private final GroupRepository groupRepository;

	public static int getCurrentWeek() {
		int currentWeek = 0;

		try {
			URL url = new URI(currentWeekUrl).toURL();
			currentWeek = Integer.parseInt(IOUtils.toString(url, StandardCharsets.UTF_8));
		} catch(URISyntaxException | IOException e) {
			System.err.println("Cannot get current week in GroupScheduleService.makeGetGroupScheduleRequest");
			e.printStackTrace();
		}

		return currentWeek;
	}

	// call this method from /get_schedule controller (method is responsible for schedule forming)
	public List<ScheduleEntity> getGroupScheduleList(int groupNumber) {

		// check if schedule for exactly group and current week exists in DB
		int currentWeek = getCurrentWeek();
		List<ScheduleEntity> scheduleEntityList = getGroupScheduleFromDbByGroupNumber(groupNumber);

		// if scheduleEntityList is null -> there is no record in DB -> if it has something just return it
		if(scheduleEntityList != null) {
			return scheduleEntityList;
		}

		JSONArray groupScheduleJson = getGroupSchedule(groupNumber);
		List<ScheduleEntity> groupScheduleList = null;

		try {
			groupScheduleList = createGroupScheduleList(groupScheduleJson, groupNumber, currentWeek);
		} catch(NullPointerException e) {
			System.err.println("Exception in getGroupScheduleList");
			e.printStackTrace();
		}

		// add new record to DB
		addScheduleToDb(groupScheduleList, groupNumber);

		return getGroupScheduleFromDbByGroupNumber(groupNumber);
	}

	public static JSONArray getGroupSchedule(int groupNumber) {
		String groupScheduleJson = makeGetGroupScheduleRequest(groupScheduleUrl, groupNumber);

		JSONArray resultArray = new JSONArray();

		try {
			JSONObject scheduleData = new JSONObject(groupScheduleJson);
			JSONObject schedules = scheduleData.getJSONObject("schedules");

			for (String dayOfWeek : schedules.keySet()) {
				JSONArray daySchedule = schedules.getJSONArray(dayOfWeek);
				for (int i = 0; i < daySchedule.length(); i++) {
					JSONObject lesson = daySchedule.getJSONObject(i);

					// if lesson IS NOT lab
					if(!lesson.getString("lessonTypeAbbrev").equals("ЛР")) {
						continue;
					}

					resultArray.put(new JSONObject()
							.put("subject", lesson.getString("subject"))
							.put("subjectFullName", lesson.getString("subjectFullName"))
							.put("startLessonTime", lesson.getString("startLessonTime"))
							.put("lessonType", lesson.getString("lessonTypeAbbrev"))
							.put("weekNumber", lesson.getJSONArray("weekNumber"))
							.put("subgroup", lesson.getInt("numSubgroup"))
							.put("dayOfWeek", dayOfWeek));
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in getGroupSchedule()");
			e.printStackTrace();
		}

		return resultArray;
	}

	// method creates schedule list for exactly group for current week
	private List<ScheduleEntity> createGroupScheduleList(JSONArray groupScheduleJson, int groupNumber,
												   int currentWeek)
	{
		List<ScheduleEntity> scheduleList = new ArrayList<>();

		for(int i = 0; i < groupScheduleJson.length(); ++i) {
			// create record for each subject of week
			ScheduleEntity dayOfWeekSubjectSchedule = createDayOfWeekSubjectSchedule(groupScheduleJson.getJSONObject(i),
																				groupNumber, currentWeek);
			if(dayOfWeekSubjectSchedule != null) {
				scheduleList.add(dayOfWeekSubjectSchedule);
			}
		}

		return scheduleList;
	}

	// method creates schedule record for ine subject
	private ScheduleEntity createDayOfWeekSubjectSchedule(JSONObject groupScheduleJson, int groupNumber,
													int currentWeek)
	{
		JSONArray weekNumbers = groupScheduleJson.getJSONArray("weekNumber");
		if(!hasValue(weekNumbers, currentWeek)) {
			return null;
		}

		String subject = groupScheduleJson.getString("subject");
		String subjectFullName = groupScheduleJson.getString("subjectFullName");
		SubgroupType subgroupType = SubgroupType.getSubgroupType(groupScheduleJson.getInt("subgroup"));
		LocalTime startTime = LocalTime.parse(groupScheduleJson.getString("startLessonTime"));

		return new ScheduleEntity(
				subject,
				subjectFullName,
				subgroupType,
				startTime,
				groupRepository.getGroupIdByNumber(groupNumber),
				WeekType.getCurrentWeekType(),
				DayOfWeek.getDayOfWeekByName(groupScheduleJson.getString("dayOfWeek")));
	}

	private void addScheduleToDb(List<ScheduleEntity> groupScheduleList, int groupNumber) {
		if(groupScheduleList == null) {
			return;
		}

		for(ScheduleEntity schedule : groupScheduleList) {
			schedule.setGroupId(groupRepository.getGroupIdByNumber(groupNumber));
			scheduleRepository.save(schedule);
		}
	}

	private static String makeGetGroupScheduleRequest(String str, Integer groupNumber) {
		String response = null;
		try {
			URL url = new URI(str + groupNumber.toString()).toURL();
			response = IOUtils.toString(url, StandardCharsets.UTF_8);
		} catch(URISyntaxException | IOException e) {
			System.err.println("Error in GroupScheduleService.makeGetGroupScheduleRequest");
			e.printStackTrace();
		}
		return response;
	}

	// List of current week exactly group schedule from configured json (bsuir api)
	private static boolean hasValue(JSONArray array, int value) {
		for(int i = 0; i < array.length(); ++i) {
			if(array.getInt(i) == value) {
				return true;
			}
		}
		return false;
	}

	private boolean isGroupScheduleInDb(int groupNumber) {
		return scheduleRepository.isScheduleInDbByGroupNumber(groupNumber, WeekType.getCurrentWeekType());
	}

	private List<ScheduleEntity> getGroupScheduleFromDbByGroupNumber(int groupNumber) {
		if(isGroupScheduleInDb(groupNumber)) {
			return scheduleRepository.getScheduleEntityByGroupNumber(groupNumber, WeekType.getCurrentWeekType());
		}
		return null;
	}
}
