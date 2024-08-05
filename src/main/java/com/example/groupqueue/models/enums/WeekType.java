package com.example.groupqueue.models.enums;

import static com.example.groupqueue.services.ScheduleService.getCurrentWeek;

public enum WeekType {
	FIRST,
	SECOND,
	THIRD,
	FOURTH;

	public static WeekType getCurrentWeekType() {
		return switch(getCurrentWeek()) {
			case 1 -> FIRST;
			case 2 -> SECOND;
			case 3 -> THIRD;
			case 4 -> FOURTH;
			default -> null;
		};
	}
}
