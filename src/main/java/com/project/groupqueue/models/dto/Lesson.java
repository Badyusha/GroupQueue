package com.project.groupqueue.models.dto;

import com.project.groupqueue.models.enums.DayOfWeek;
import com.project.groupqueue.models.enums.SubgroupType;
import com.project.groupqueue.models.enums.WeekType;
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

	public Lesson(long lessonId,
				  String subjectName,
				  String subjectFullName,
				  SubgroupType subgroupType,
				  LocalTime startTime,
				  boolean isRegisteredInQueue,
				  Integer numberInQueue,
				  Long queueId,
				  DayOfWeek dayOfWeek,
				  LocalDate date,
				  WeekType weekType) {
		this.lessonId = lessonId;
		this.subjectName = subjectName;
		this.subjectFullName = subjectFullName;
		this.subgroupType = subgroupType;
		this.startTime = startTime;
		this.isRegisteredInQueue = isRegisteredInQueue;
		this.numberInQueue = numberInQueue;
		this.queueId = queueId;
		this.dayOfWeek = dayOfWeek;
		this.date = date;
		this.weekType = weekType;
	}
}
