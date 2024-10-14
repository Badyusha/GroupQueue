package com.project.groupqueue.external.api;

import com.project.groupqueue.exceptions.ScheduleException;
import com.project.groupqueue.models.entities.ScheduleEntity;
import com.project.groupqueue.models.enums.DayOfWeek;
import com.project.groupqueue.models.enums.SubgroupType;
import com.project.groupqueue.models.enums.WeekType;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BsuirAPI {
	private static final String BASE_URL = "https://iis.bsuir.by/api/v1";
	private static final String GROUP_SCHEDULE_URL = BASE_URL + "/schedule?studentGroup=";
	private static final String CURRENT_WEEK_URL = BASE_URL + "/schedule/current-week";

	public static int getCurrentWeek() {
		return Integer.parseInt(makeRequest(CURRENT_WEEK_URL));
	}

	public static List<ScheduleEntity> getScheduleEntities(long groupId, int groupNumber) {
		String groupScheduleJson = makeRequest(GROUP_SCHEDULE_URL + groupNumber);
		JSONObject scheduleData = new JSONObject(groupScheduleJson);
		JSONObject schedules = scheduleData.getJSONObject("schedules");

		List<ScheduleEntity> scheduleEntities = new ArrayList<>();
		for (String dayOfWeekStr : schedules.keySet()) {
			DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeekByName(dayOfWeekStr);
			JSONArray daySchedule = schedules.getJSONArray(dayOfWeekStr);
			for (int i = 0; i < daySchedule.length(); i++) {
				JSONObject lesson = daySchedule.getJSONObject(i);

				// if lesson IS NOT lab
				if (!lesson.getString("lessonTypeAbbrev").equals("ЛР")) {
					continue;
				}

				String subjectName = lesson.getString("subject");
				String subjectFullName = lesson.getString("subjectFullName");
				SubgroupType subgroupType = SubgroupType.getSubgroupType(lesson.getInt("numSubgroup"));
				LocalTime startTime = LocalTime.parse(lesson.getString("startLessonTime"));
				JSONArray weekNumbers = lesson.getJSONArray("weekNumber");
				for(int j = 0; j < weekNumbers.length(); ++j) {
					WeekType weekType = WeekType.getWeekTypeByNumber(weekNumbers.getInt(j));
					scheduleEntities.add(new ScheduleEntity(
															subjectName,
															subjectFullName,
															subgroupType,
															startTime,
															groupId,
															weekType,
															dayOfWeek));
				}
			}
		}
		return scheduleEntities;
	}


	public static boolean isGroupExist(int groupNumber) {
		return checkRequest(GROUP_SCHEDULE_URL + groupNumber);
	}


	private static boolean checkRequest(String uri) {
		try {
			URL url = new URI(uri).toURL();
			IOUtils.toString(url, StandardCharsets.UTF_8);
		} catch(URISyntaxException | IOException e) {
			return false;
		}
		return true;
	}

	private static String makeRequest(String uri) {
		String response;
		try {
			URL url = new URI(uri).toURL();
			response = IOUtils.toString(url, StandardCharsets.UTF_8);
			if (response == null) {
				throw new ScheduleException("response is null in makeGetGroupScheduleRequest()");
			}
		} catch(URISyntaxException | IOException e) {
			throw new ScheduleException("cannot get data from request");
		}
		return response;
	}
}
