package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.enums.SubgroupType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class QueueInfo {
	private String subjectName;
	private String subjectFullName;
	private LocalTime startTime;
	private LocalDate date;
	private SubgroupType subgroupType;
	private byte[] passingLabs;
	private int numberInQueue;
	private int totalStudentsNumber;
}
