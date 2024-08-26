package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.QueueException;
import com.example.groupqueue.models.dto.PreQueue;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.repo.PreQueueRepository;
import com.example.groupqueue.utils.CookieUtil;
import com.example.groupqueue.utils.GenerateQueueUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PreQueueService {
	private final PreQueueRepository preQueueRepository;

	public void removeUserFromPreQueueByLessonId(HttpServletRequest request, long lessonId) {
		long userId = CookieUtil.getUserId(request);
		preQueueRepository.delete(preQueueRepository.getPreQueueEntityByUserIdLessonId(userId, lessonId));
	}

	public void addUserToPreQueue(HttpServletRequest request, PreQueue preQueue) {
		long userId = CookieUtil.getUserId(request);
		LocalTime startTime = LocalTime.parse(preQueue.getStartTime());
		DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeekByName(preQueue.getDayOfWeek());
		if(GenerateQueueUtil.isRegistrationOpen(dayOfWeek, startTime)) {
			preQueueRepository.save(preQueue.toPreQueueEntity(userId));
			return;
		}
		throw new QueueException("cannot register to pre_queue because of current day of week or current time");
	}
}