package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Schedule {
	private String subject;
	private String subjectFullName;
	private SubgroupType subgroupType;
	private LocalTime startTime;
	private Integer groupNumber;
	private Long groupId;
	private WeekType weekType;
	private DayOfWeek dayOfWeek;

	public static List<Schedule> convertScheduleEntityListToScheduleList(List<ScheduleEntity> scheduleEntityList) {
		List<Schedule> scheduleList = new ArrayList<>();
		for(ScheduleEntity scheduleEntity : scheduleEntityList) {
			scheduleList.add(new Schedule(scheduleEntity));
		}
		return scheduleList;
	}

	public Schedule(ScheduleEntity scheduleEntity) {
		this.subject = scheduleEntity.getSubjectName();
		this.subjectFullName = scheduleEntity.getSubjectFullName();
		this.subgroupType = scheduleEntity.getSubgroupType();
		this.startTime = scheduleEntity.getStartTime();
		this.groupNumber = null;
		this.groupId = scheduleEntity.getGroupId();
		this.weekType = scheduleEntity.getWeekType();
		this.dayOfWeek = scheduleEntity.getDayOfWeek();
	}
}
