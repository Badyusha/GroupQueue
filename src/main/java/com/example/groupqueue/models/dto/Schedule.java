package com.example.groupqueue.models.dto;

import lombok.Data;

@Data
public class Schedule {
	private DayOfWeekScheduled monday;
	private DayOfWeekScheduled tuesday;
	private DayOfWeekScheduled wednesday;
	private DayOfWeekScheduled thursday;
	private DayOfWeekScheduled friday;
	private DayOfWeekScheduled saturday;
	private DayOfWeekScheduled sunday;
}
