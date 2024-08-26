package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.ScheduleException;
import com.example.groupqueue.external.api.BsuirAPI;
import com.example.groupqueue.models.dto.DayOfWeekScheduled;
import com.example.groupqueue.models.dto.GroupSchedule;
import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.models.entities.PreQueueEntity;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.PreQueueRepository;
import com.example.groupqueue.repo.QueueRepository;
import com.example.groupqueue.repo.ScheduleRepository;
import com.example.groupqueue.utils.CookieUtil;
import com.example.groupqueue.utils.GenerateQueueUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final GroupRepository groupRepository;
	private final PreQueueRepository preQueueRepository;
	private final QueueRepository queueRepository;
	private final LessonService lessonService;

	public List<GroupSchedule> getGroupSchedulesByGroupId(long groupId) {
		return scheduleRepository.getGroupSchedulesByGroupId(groupId);
	}

	public Schedule getDayOfWeekSchedule(HttpServletRequest request) {
		long userId = CookieUtil.getUserId(request);
		long groupId = CookieUtil.getGroupId(request);
		List<Lesson> lessonList = lessonService.getScheduleInfoByUserIdGroupId(userId, groupId);
		if (lessonList.isEmpty()) {
			throw new ScheduleException("there is no schedule for groupId=" + groupId);
		}

		WeekType weekType = lessonList.getFirst().getWeekType();
		return fillInSchedule(lessonList, weekType);
	}

	private Schedule fillInSchedule(List<Lesson> lessonList, WeekType weekType) {
		Schedule schedule = new Schedule(weekType);
		for(Lesson lesson : lessonList) {
			DayOfWeek dayOfWeek = lesson.getDayOfWeek();
			DayOfWeekScheduled dayOfWeekScheduled = switch(dayOfWeek) {
				case MONDAY -> schedule.getMonday();
				case TUESDAY -> schedule.getTuesday();
				case WEDNESDAY -> schedule.getWednesday();
				case THURSDAY -> schedule.getThursday();
				case FRIDAY -> schedule.getFriday();
				case SATURDAY -> schedule.getSaturday();
				case SUNDAY -> schedule.getSunday();
			};
			lesson.setRegistrationOpen(GenerateQueueUtil.isRegistrationOpen(dayOfWeek, lesson.getStartTime()));
			dayOfWeekScheduled.addLesson(lesson);
		}
		return schedule;
	}
	
	//	code below add records into schedule and lesson tables ===
	public int getCurrentWeek() {
		return BsuirAPI.getCurrentWeek();
	}

	public void addRecordsForNewGroupByGroupNumber(int groupNumber) {
		int scheduleWeekNumber = getCurrentWeek();
		if (isTimeToGenerateNextWeekSchedule()) {
			scheduleWeekNumber = WeekType.getNextWeekNumber(scheduleWeekNumber);
		}

		Long groupId = groupRepository.getGroupIdByNumber(groupNumber);
		List<ScheduleEntity> scheduleEntities = BsuirAPI.getScheduleEntities(groupId, groupNumber);
		scheduleRepository.saveAll(scheduleEntities);

		WeekType weekType = WeekType.getWeekTypeByNumber(scheduleWeekNumber);
		List<ScheduleEntity> scheduleEntityList =
				getScheduleEntityByGroupNumberWeekType(groupNumber, weekType);
		lessonService.addLessonByScheduleList(weekType, scheduleEntityList);
	}

	public List<ScheduleEntity> getScheduleEntityByGroupNumberWeekType(int groupNumber, WeekType week) {
		return scheduleRepository.getScheduleEntityByGroupNumberWeekType(groupNumber, week);
	}

	private boolean isTimeToGenerateNextWeekSchedule() {
		DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeekFromCalendar();
		if(!dayOfWeek.equals(DayOfWeek.SUNDAY)) {
			return false;
		}

		LocalTime generateNewScheduleTime = DayOfWeek.TIME_TO_GENERATE_NEW_SCHEDULE;
		return !LocalTime.now().isBefore(generateNewScheduleTime);
	}

	// SCHEDULED
	/**
	method calls every Sunday at 18:00
	*/
	@Scheduled(cron = "0 0 18 * * SUN", zone = "Europe/Moscow")
	@Transactional
	public void updateLessonsForNextWeek() {
		lessonService.deleteAll();
		addLessonsForNextWeek();
	}

	private void addLessonsForNextWeek() {
		WeekType weekType = WeekType.getNextWeekType();
		List<ScheduleEntity> scheduleEntityList = scheduleRepository.getScheduleEntityListByWeekType(weekType);
		lessonService.addLessonByScheduleList(weekType, scheduleEntityList);
	}
}
