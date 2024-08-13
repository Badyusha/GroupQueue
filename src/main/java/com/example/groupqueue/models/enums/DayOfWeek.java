package com.example.groupqueue.models.enums;

import com.example.groupqueue.exceptions.DayOfWeekException;

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

	public final String day;

	DayOfWeek(String day) {
		this.day = day;
	}

	public static DayOfWeek getDayOfWeekByName(String name) throws DayOfWeekException {
		return switch (name) {
			case "Понедельник" -> MONDAY;
			case "Вторник" -> TUESDAY;
			case "Среда" -> WEDNESDAY;
			case "Четверг" -> THURSDAY;
			case "Пятница" -> FRIDAY;
			case "Суббота" -> SATURDAY;
			case "Воскресенье" -> SUNDAY;
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
			default -> throw new DayOfWeekException("Day of week with name=" + dayOfWeek + " not found");
		};
	}

	public static java.time.DayOfWeek getJavaTimeDayOfWeek(DayOfWeek day) {
		return switch(day) {
			case MONDAY -> java.time.DayOfWeek.MONDAY;
			case TUESDAY -> java.time.DayOfWeek.TUESDAY;
			case WEDNESDAY -> java.time.DayOfWeek.WEDNESDAY;
			case THURSDAY -> java.time.DayOfWeek.THURSDAY;
			case FRIDAY -> java.time.DayOfWeek.FRIDAY;
			case SATURDAY -> java.time.DayOfWeek.SATURDAY;
			case SUNDAY -> java.time.DayOfWeek.SUNDAY;
		};
	}
}
