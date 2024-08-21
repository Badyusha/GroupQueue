package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.QueueException;
import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.dto.PreQueue;
import com.example.groupqueue.models.entities.PreQueueEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.repo.PreQueueRepository;
import com.example.groupqueue.utils.CookieUtils;
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
		long userId = CookieUtils.getUserId(request);
		preQueueRepository.delete(preQueueRepository.getPreQueueEntityByUserIdLessonId(userId, lessonId));
	}

	public void addUserToPreQueue(HttpServletRequest request, PreQueue preQueue) {
		long userId = CookieUtils.getUserId(request);
		LocalTime startTime = LocalTime.parse(preQueue.getStartTime());
		DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeekByName(preQueue.getDayOfWeek());
		if(isRegistrationOpen(dayOfWeek, startTime)) {
			preQueueRepository.save(preQueue.toPreQueueEntity(userId));
			return;
		}
		throw new QueueException("cannot register to pre_queue because of current day of week or current time");
	}

	public static boolean isRegistrationOpen(DayOfWeek dayOfWeek, LocalTime startTime) {
		LocalTime now = LocalTime.now();
		java.time.DayOfWeek todayDayOfWeek = LocalDate.now().getDayOfWeek();
		java.time.DayOfWeek dayOfWeek_JavaTime = DayOfWeek.getJavaTimeDayOfWeek(dayOfWeek);
		boolean isTodayDayBeforeLab = todayDayOfWeek.plus(1).equals(dayOfWeek_JavaTime);
		boolean isNowTimeForRegistration = !now.isBefore(DayOfWeek.TIME_FOR_REGISTRATION);

		boolean isTodayLabDay = todayDayOfWeek.equals(dayOfWeek_JavaTime);
		boolean isNowTimeBeforeLabStart = !now.isAfter(startTime.minusHours(1));

		return (isTodayDayBeforeLab && isNowTimeForRegistration) || (isTodayLabDay && isNowTimeBeforeLabStart);
	}
}