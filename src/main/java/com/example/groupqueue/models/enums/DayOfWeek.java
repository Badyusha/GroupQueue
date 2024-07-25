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
}
