package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.ScheduleException;
import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
	private final LessonRepository lessonRepository;

	public List<Lesson> getScheduleInfoByUserIdGroupId(long userId, long groupId) {
		List<Object[]> lessons = lessonRepository.getScheduleInfoByUserIdGroupId(userId, groupId);
		List<Lesson> lessonList = new ArrayList<>(lessons.size());
		for(Object[] lesson : lessons) {
			long lessonId = (Long) lesson[0];
			String subjectName = (String) lesson[1];
			String subjectFullName = (String) lesson[2];
			DayOfWeek dayOfWeek = (DayOfWeek) lesson[3];
			SubgroupType subgroupType = (SubgroupType) lesson[4];
			LocalDate date = (LocalDate) lesson[5];
			WeekType weekType = (WeekType) lesson[6];
			LocalTime startTime = (LocalTime) lesson[7];
			Integer numberInQueue = (Integer) lesson[8];
			Long queueId = (Long) lesson[9];
			boolean isRegisteredInQueue = (Boolean) lesson[10];
			boolean isRegistrationOpen = PreQueueService.isRegistrationOpen(dayOfWeek, startTime);

			lessonList.add(new Lesson(
					lessonId,
					subjectName,
					subjectFullName,
					subgroupType,
					startTime,
					isRegisteredInQueue,
					numberInQueue,
					queueId,
					dayOfWeek,
					date,
					weekType,
					isRegistrationOpen));
		}
		return lessonList;
	}

	public void addLessonByScheduleList(WeekType weekType, List<ScheduleEntity> scheduleEntityList) {
		if(scheduleEntityList == null || scheduleEntityList.isEmpty()) {
			throw new ScheduleException("scheduleEntityList is null or empty. Probably there is no lessons on the week");
		}

		for(ScheduleEntity scheduleEntity : scheduleEntityList) {
			DayOfWeek dayOfWeek = scheduleEntity.getDayOfWeek();
			lessonRepository.save(new LessonEntity(
					scheduleEntity.getId(),
					SortType.SIMPLE,
					DayOfWeek.getLessonDate(dayOfWeek, weekType)
			));
		}
	}
}
