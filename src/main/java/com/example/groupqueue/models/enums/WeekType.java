package com.example.groupqueue.models.enums;

import com.example.groupqueue.exceptions.WeekTypeException;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

public enum WeekType {
	FIRST,
	SECOND,
	THIRD,
	FOURTH;

	private static final String CURRENT_WEEK_URL = "https://iis.bsuir.by/api/v1/schedule/current-week";
	public static final LocalTime TIME_TO_GENERATE_NEW_SCHEDULE = LocalTime.of(18, 0);

	public static WeekType getCurrentWeekType() throws WeekTypeException {
		return switch(getCurrentWeekViaApi()) {
			case 1 -> FIRST;
			case 2 -> SECOND;
			case 3 -> THIRD;
			case 4 -> FOURTH;
			default -> throw new WeekTypeException("failed to get current week type");
		};
	}

	public static int getWeekNumber(WeekType week) {
		return switch(week) {
			case FIRST -> 1;
			case SECOND -> 2;
			case THIRD -> 3;
			case FOURTH -> 4;
		};
	}

	public static int getNextWeekNumber(int week) {
		int nextWeekNumber = week + 1;
		return switch(nextWeekNumber) {
			case 2 -> 2;
			case 3 -> 3;
			case 4 -> 4;
			case 5 -> 1;
			default -> throw new WeekTypeException("failed to get next week number");
		};
	}

	public static WeekType getNextWeekType() throws WeekTypeException {
		int nextWeek = getCurrentWeekViaApi() + 1;
		return switch(nextWeek) {
			case 2 -> SECOND;
			case 3 -> THIRD;
			case 4 -> FOURTH;
			case 5 -> FIRST;
			default -> throw new WeekTypeException("failed to get next week type");
		};
	}

	public static int getCurrentWeekViaApi() {
		int currentWeek = 0;
		try {
			URL url = new URI(CURRENT_WEEK_URL).toURL();
			currentWeek = Integer.parseInt(IOUtils.toString(url, StandardCharsets.UTF_8));
		} catch(URISyntaxException | IOException e) {
			System.err.println("Cannot get current week in GroupScheduleService.makeGetGroupScheduleRequest");
			e.printStackTrace();
		}
		return currentWeek;
	}

	public static WeekType getWeekTypeByNumber(int weekNumber) {
		return switch(weekNumber) {
			case 1 -> FIRST;
			case 2 -> SECOND;
			case 3 -> THIRD;
			case 4 -> FOURTH;
			default -> throw new WeekTypeException("failed to get week type by number");
		};
	}
}
