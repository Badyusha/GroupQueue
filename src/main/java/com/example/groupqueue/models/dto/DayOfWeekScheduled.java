package com.example.groupqueue.models.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DayOfWeekScheduled {
	private LocalDate date;
	private List<Lesson> lessons;
}
