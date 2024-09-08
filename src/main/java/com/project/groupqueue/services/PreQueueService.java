package com.project.groupqueue.services;

import com.project.groupqueue.exceptions.QueueException;
import com.project.groupqueue.models.dto.PreQueue;
import com.project.groupqueue.models.entities.PreQueueEntity;
import com.project.groupqueue.models.enums.DayOfWeek;
import com.project.groupqueue.repo.PreQueueRepository;
import com.project.groupqueue.utils.CookieUtil;
import com.project.groupqueue.utils.GenerateQueueUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PreQueueService {
	private final PreQueueRepository preQueueRepository;

	public void removeStudentFromPreQueueByLessonId(HttpServletRequest request, long lessonId) {
		long studentId = CookieUtil.getStudentId(request);
		preQueueRepository.delete(preQueueRepository.getPreQueueEntityByStudentIdLessonId(studentId, lessonId));
	}

	public void addStudentToPreQueue(HttpServletRequest request, PreQueue preQueue) {
		long studentId = CookieUtil.getStudentId(request);
		LocalTime startTime = LocalTime.parse(preQueue.getStartTime());
		DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeekByName(preQueue.getDayOfWeek());
		if(GenerateQueueUtil.isRegistrationOpen(dayOfWeek, startTime)) {
			preQueueRepository.save(preQueue.toPreQueueEntity(studentId));
			return;
		}
		throw new QueueException("cannot register to pre_queue because of current day of week or current time");
	}

	public void changePassingLabs(HttpServletRequest request, PreQueue preQueue) {
		PreQueueEntity preQueueEntity =
				preQueueRepository.getPreQueueEntityByStudentIdLessonId(CookieUtil.getStudentId(request),
																		preQueue.getLessonId());
		preQueueEntity.setPassingLabs(preQueue.getPassingLabs());
		preQueueRepository.save(preQueueEntity);
	}
}