package com.example.groupqueue.models.enums;

import com.example.groupqueue.exceptions.WeekTypeException;
import com.example.groupqueue.external.api.BsuirAPI;

public enum WeekType {
	FIRST,
	SECOND,
	THIRD,
	FOURTH;

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
		int nextWeek = BsuirAPI.getCurrentWeek() + 1;
		return switch(nextWeek) {
			case 2 -> SECOND;
			case 3 -> THIRD;
			case 4 -> FOURTH;
			case 5 -> FIRST;
			default -> throw new WeekTypeException("failed to get next week type");
		};
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
