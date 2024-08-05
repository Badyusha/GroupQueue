package com.example.groupqueue.models.enums;

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

	public static DayOfWeek getDayOfWeekByName(String name) {
		return switch (name) {
			case "Понедельник" -> MONDAY;
			case "Вторник" -> TUESDAY;
			case "Среда" -> WEDNESDAY;
			case "Четверг" -> THURSDAY;
			case "Пятница" -> FRIDAY;
			case "Суббота" -> SATURDAY;
			case "Воскресенье" -> SUNDAY;
			default -> null;
		};
	}
}
