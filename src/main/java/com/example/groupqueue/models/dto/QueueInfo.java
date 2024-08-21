package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.models.enums.SubgroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class QueueInfo {
	private String subjectName;
	private String subjectFullName;
	private LocalDate date;
	private LocalTime startTime;
	private SortType sortType;
	private SubgroupType subgroupType;
	private byte[] passingLabs;
	private Integer numberInQueue;
	private Long lessonId;
}