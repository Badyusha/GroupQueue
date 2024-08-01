package com.example.groupqueue.services;

import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class GroupService {
	private final GroupRepository groupRepository;

	@Autowired
	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	private static final String groupScheduleUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";

	public HttpStatusCode makeGroupExistsRequest(Integer groupNumber) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			restTemplate.getForEntity(groupScheduleUrl + groupNumber.toString(), String.class);
		} catch(HttpClientErrorException | HttpServerErrorException e) {
			return HttpStatus.NOT_FOUND;
		}

		groupRepository.saveGroup(groupNumber);
		return HttpStatus.OK;
	}

	public static int makeGetCurrentWeekRequest() {
		int currentWeek = 0;
		String url = "https://iis.bsuir.by/api/v1/schedule/current-week";

		try {
			URL currentWeekUrl = new URI(url).toURL();
			currentWeek = Integer.parseInt(IOUtils.toString(currentWeekUrl, StandardCharsets.UTF_8));
		} catch(URISyntaxException | IOException e) {
			System.err.println("Error in GroupScheduleService.makeGetGroupScheduleRequest");
			e.printStackTrace();
		}

		return currentWeek;
	}

	public boolean isGroupInDb(Integer groupNumber) {
		return groupRepository.isGroupInDb(groupNumber);
	}

	public static JSONArray getGroupSchedule(int groupNumber) {
		String groupScheduleJson = makeGetGroupScheduleRequest(groupScheduleUrl, groupNumber);

		if(groupScheduleJson == null) {
			return null;
		}

		JSONObject groupScheduleJsonObject = new JSONObject(groupScheduleJson);
		JSONObject schedule = groupScheduleJsonObject.getJSONObject("schedules");
		JSONArray groupSchedule = new JSONArray();

		for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
			JSONObject dayOfWeekSchedule = getDayOfWeekScheduleInfo(schedule, dayOfWeek.day);

			if(dayOfWeekSchedule != null) {
				groupSchedule.put(dayOfWeekSchedule);
			}
		}

		return groupSchedule;
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

	private static JSONObject getDayOfWeekScheduleInfo(JSONObject scheduleJsonObject, String dayOfWeek) {
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
			schedule.put(fillScheduleObject(subjectInfo));
		}

		JSONObject dayOfWeekScheduleInfo = new JSONObject();
		dayOfWeekScheduleInfo.put(dayOfWeek, schedule);

		return dayOfWeekScheduleInfo;
	}

	private static JSONObject fillScheduleObject(JSONObject subjectInfo) {
		JSONObject schedule = new JSONObject();

		schedule.put("subject", subjectInfo.getString("subject"));
		schedule.put("subjectFullName", subjectInfo.getString("subjectFullName"));
		schedule.put("subgroup", subjectInfo.getInt("numSubgroup"));
		schedule.put("startLessonTime", subjectInfo.getString("startLessonTime"));
		schedule.put("lessonType", subjectInfo.getString("lessonTypeAbbrev"));
		schedule.put("weekNumber", subjectInfo.getJSONArray("weekNumber"));

		return schedule;
	}
}
