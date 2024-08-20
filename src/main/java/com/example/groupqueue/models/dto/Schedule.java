package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.WeekType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Schedule {
	private DayOfWeekScheduled monday;
	private DayOfWeekScheduled tuesday;
	private DayOfWeekScheduled wednesday;
	private DayOfWeekScheduled thursday;
	private DayOfWeekScheduled friday;
	private DayOfWeekScheduled saturday;
	private DayOfWeekScheduled sunday;

	public Schedule(WeekType weekType) {
		monday = new DayOfWeekScheduled(weekType, DayOfWeek.MONDAY);
		tuesday = new DayOfWeekScheduled(weekType, DayOfWeek.TUESDAY);
		wednesday = new DayOfWeekScheduled(weekType, DayOfWeek.WEDNESDAY);
		thursday = new DayOfWeekScheduled(weekType, DayOfWeek.THURSDAY);
		friday = new DayOfWeekScheduled(weekType, DayOfWeek.FRIDAY);
		saturday = new DayOfWeekScheduled(weekType, DayOfWeek.SATURDAY);
		sunday = new DayOfWeekScheduled(weekType, DayOfWeek.SUNDAY);
	}
}
