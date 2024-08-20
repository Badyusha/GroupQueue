package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.PreQueueEntity;
import lombok.Data;

@Data
public class PreQueue {
	long id;
	long lessonId;
	long userId;
	String dayOfWeek;
	String startTime;
	byte[] passingLabs;

	public PreQueueEntity toPreQueueEntity(long userId) {
		return new PreQueueEntity(lessonId, userId, passingLabs);
	}
}
