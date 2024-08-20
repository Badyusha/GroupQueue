package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Lesson {
	private long lessonId;
	private String subjectName;
	private String subjectFullName;
	private SubgroupType subgroupType;
	private LocalTime startTime;
	private boolean isRegisteredInQueue;
	private Integer numberInQueue;
	private Long queueId;

	private DayOfWeek dayOfWeek;
	private LocalDate date;
	private WeekType weekType;
	private boolean isRegistrationOpen;
}
