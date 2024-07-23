package com.example.groupqueue.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URISyntaxException;

public class GroupScheduleService {
	private static int mageGetCurrentWeekRequest() {
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

	public static JSONArray getGroupSchedule(int groupNumber) {
		String url = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";
		String groupScheduleJson = makeGetGroupScheduleRequest(url, groupNumber);

		JSONObject groupScheduleJsonObject = new JSONObject(groupScheduleJson);
		JSONObject schedule = groupScheduleJsonObject.getJSONObject("schedules");
		JSONArray groupSchedule = new JSONArray();
		String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

		for(String dayOfWeek : daysOfWeek) {
			JSONObject dayOfWeekSchedule = getDayOfWeekScheduleInfo(schedule, dayOfWeek);

			if(dayOfWeekSchedule != null) {
				groupSchedule.put(dayOfWeekSchedule);
			}
		}

		return groupSchedule;
	}
}
