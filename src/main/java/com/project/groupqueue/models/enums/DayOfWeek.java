package com.project.groupqueue.models.enums;

import com.project.groupqueue.exceptions.DayOfWeekException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public enum DayOfWeek {
	MONDAY ("Понедельник"),
	TUESDAY ("Вторник"),
	WEDNESDAY ("Среда"),
	THURSDAY ("Четверг"),
	FRIDAY ("Пятница"),
	SATURDAY ("Суббота"),
	SUNDAY ("Воскресенье");

	DayOfWeek(String day) {
		this.day = day;
	}

	public final String day;
	public static final LocalTime TIME_TO_GENERATE_NEW_SCHEDULE = LocalTime.of(22, 0);
	public static final LocalTime TIME_FOR_REGISTRATION = LocalTime.of(22, 0);

	public static DayOfWeek getDayOfWeekByName(String name) throws DayOfWeekException {
		return switch (name) {
			case "Понедельник", "monday" -> MONDAY;
			case "Вторник", "tuesday" -> TUESDAY;
			case "Среда", "wednesday" -> WEDNESDAY;
			case "Четверг", "thursday" -> THURSDAY;
			case "Пятница", "friday" -> FRIDAY;
			case "Суббота", "saturday" -> SATURDAY;
			case "Воскресенье", "sunday" -> SUNDAY;

			default -> throw new DayOfWeekException("Day of week with name=" + name + " not found");
		};
	}

	public static DayOfWeek getDayOfWeekFromCalendar() throws DayOfWeekException {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return switch(dayOfWeek) {
			case Calendar.SUNDAY -> SUNDAY;
			case Calendar.MONDAY -> MONDAY;
			case Calendar.TUESDAY -> TUESDAY;
			case Calendar.WEDNESDAY -> WEDNESDAY;
			case Calendar.THURSDAY -> THURSDAY;
			case Calendar.FRIDAY -> FRIDAY;
			case Calendar.SATURDAY -> SATURDAY;
			default -> throw new DayOfWeekException("Day of week with number=" + dayOfWeek + " not found");
		};
	}

	public static java.time.DayOfWeek getJavaTimeDayOfWeek(DayOfWeek day) {
		return java.time.DayOfWeek.valueOf(day.toString());
	}

	public static LocalDate getLessonDate(DayOfWeek dayOfWeek, WeekType week) {
		int weeksToAdd = 0;
		if(week.equals(WeekType.getNextWeekType())) {
			weeksToAdd = 1;
		}

		LocalDate today = LocalDate.now();
		java.time.DayOfWeek targetDay = DayOfWeek.getJavaTimeDayOfWeek(dayOfWeek);

		return today.with(targetDay).plusWeeks(weeksToAdd);
	}
}
