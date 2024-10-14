package com.project.groupqueue.models.dto;

import com.project.groupqueue.models.enums.SortType;
import com.project.groupqueue.models.enums.SubgroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class GroupSchedule {
	private long lessonId;
	private String subjectName;
	private String subjectFullName;
	private SubgroupType subgroupType;
	private LocalDate date;
	private LocalTime startTime;
	private SortType sortType;
}
