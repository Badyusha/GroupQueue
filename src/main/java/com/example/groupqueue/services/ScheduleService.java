package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.ScheduleRepository;
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
public class ScheduleService {
	private static final String groupScheduleUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";
	private static final String currentWeekUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";

	private final ScheduleRepository scheduleRepository;
	private final GroupRepository groupRepository;

	@Autowired
	public ScheduleService(ScheduleRepository scheduleRepository, GroupRepository groupRepository) {
		this.scheduleRepository = scheduleRepository;
		this.groupRepository = groupRepository;
	}

	public static int getCurrentWeek() {
		int currentWeek = 0;

		try {
			URL url = new URI(currentWeekUrl).toURL();
			currentWeek = Integer.parseInt(IOUtils.toString(url, StandardCharsets.UTF_8));
		} catch(URISyntaxException | IOException e) {
			System.err.println("Error in GroupScheduleService.makeGetGroupScheduleRequest");
			e.printStackTrace();
		}

		return currentWeek;
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

	public List<Schedule> getGroupScheduleList(int groupNumber) {
		// TODO check if group schedule exists in db
		int currentWeek = getCurrentWeek();
		List<ScheduleEntity> scheduleEntityList = getGroupScheduleFromDbByGroupNumber(groupNumber);

		if(scheduleEntityList != null) {
			System.err.println("There is schedules in db: ");
			return Schedule.convertScheduleEntityListToScheduleList(scheduleEntityList);
		}

		System.err.println("There is no schedule in db for "+groupNumber+" and curweek("+currentWeek+")");

		JSONArray groupScheduleJson = new JSONArray(getGroupSchedule(groupNumber));
		List<Schedule> groupScheduleList = null;
		try {
			groupScheduleList = createGroupScheduleList(groupScheduleJson, groupNumber, currentWeek);
		} catch(NullPointerException e) {
			System.err.println("Exception in getGroupScheduleList");
			e.printStackTrace();
		}

		addScheduleToDb(groupScheduleList, groupNumber);

		return groupScheduleList;
	}

	private List<Schedule> createGroupScheduleList(JSONArray groupScheduleJson,
														  int groupNumber, int currentWeek) {
		List<Schedule> scheduleList = new ArrayList<>();

		for(int i = 0; i < groupScheduleJson.length(); ++i) {
			Schedule dayOfWeekSubjectSchedule = createDayOfWeekSubjectSchedule(groupScheduleJson.getJSONObject(i),
																				groupNumber, currentWeek);
			if(dayOfWeekSubjectSchedule != null) {
				scheduleList.add(dayOfWeekSubjectSchedule);
			}
		}

		return scheduleList;
	}

	private static boolean hasValue(JSONArray array, int value) {
		for(int i = 0; i < array.length(); ++i) {
			if(array.getInt(i) == value) {
				return true;
			}
		}
		return false;
	}

	private Schedule createDayOfWeekSubjectSchedule(JSONObject groupScheduleJson,
														   int groupNumber, int currentWeek)
	{
		JSONArray weekNumbers = groupScheduleJson.getJSONArray("weekNumber");
		if(!hasValue(weekNumbers, currentWeek)) {
			return null;
		}

		return new Schedule(groupScheduleJson.getString("subject"),
				groupScheduleJson.getString("subjectFullName"),
				SubgroupType.getSubgroupType(groupScheduleJson.getInt("subgroup")),
				LocalTime.parse(groupScheduleJson.getString("startLessonTime")),
				groupNumber,
				scheduleRepository.getScheduleIdCountByGroupNumber(groupNumber, WeekType.getCurrentWeekType()),
				WeekType.getCurrentWeekType(),
				DayOfWeek.getDayOfWeekByName(groupScheduleJson.getString("dayOfWeek")));

	}

	private void addScheduleToDb(List<Schedule> groupScheduleList, int groupNumber) {
		if(groupScheduleList == null) {
			return;
		}

		for(Schedule schedule : groupScheduleList) {
			schedule.setGroupId(groupRepository.getGroupIdByNumber(groupNumber));
			ScheduleEntity ent = new ScheduleEntity(schedule);
			System.err.println(ent);
			scheduleRepository.save(ent);
		}
	}



	public static String getGroupSchedule(int groupNumber) {
		String groupScheduleJson = makeGetGroupScheduleRequest(groupScheduleUrl, groupNumber);

		if(groupScheduleJson == null) {
			return null;
		}

		JSONObject groupScheduleJsonObject = new JSONObject(groupScheduleJson);
		JSONObject schedule = groupScheduleJsonObject.getJSONObject("schedules");
		StringBuilder groupSchedule = new StringBuilder();

		for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
			JSONArray dayOfWeekSchedule = getDayOfWeekScheduleInfo(schedule, dayOfWeek.day);

			if(dayOfWeekSchedule != null && !dayOfWeekSchedule.isEmpty()) {
				groupSchedule.append(dayOfWeekSchedule);
			}
		}

		return groupSchedule.toString();
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

	private static JSONArray getDayOfWeekScheduleInfo(JSONObject scheduleJsonObject, String dayOfWeek) {
		JSONArray dayOfWeekSchedule;
		try {
			dayOfWeekSchedule = scheduleJsonObject.getJSONArray(dayOfWeek);
		} catch(JSONException e) {
			System.err.println("There is no `" + dayOfWeek + "` in the schedule list");
			return null;
		}

		JSONArray schedule = new JSONArray();
		for(int i = 0; i < dayOfWeekSchedule.length(); ++i) {
			JSONObject subjectInfo = dayOfWeekSchedule.getJSONObject(i);

			if(subjectInfo.getString("lessonTypeAbbrev").equals("ЛР")) {
				schedule.put(fillScheduleObject(subjectInfo, dayOfWeek));
			}
		}

		return schedule;
	}

	private static JSONObject fillScheduleObject(JSONObject subjectInfo, String dayOfWeek) {
		return new JSONObject()
				.put("dayOfWeek", dayOfWeek)
				.put("subject", subjectInfo.getString("subject"))
				.put("subjectFullName", subjectInfo.getString("subjectFullName"))
				.put("subgroup", subjectInfo.getInt("numSubgroup"))
				.put("startLessonTime", subjectInfo.getString("startLessonTime"))
				.put("lessonType", subjectInfo.getString("lessonTypeAbbrev"))
				.put("weekNumber", subjectInfo.getJSONArray("weekNumber"));
	}
}
