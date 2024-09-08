package com.project.groupqueue.services;

import com.project.groupqueue.exceptions.ScheduleException;
import com.project.groupqueue.models.dto.GroupSchedule;
import com.project.groupqueue.models.dto.Lesson;
import com.project.groupqueue.models.entities.LessonEntity;
import com.project.groupqueue.models.entities.ScheduleEntity;
import com.project.groupqueue.models.enums.DayOfWeek;
import com.project.groupqueue.models.enums.SortType;
import com.project.groupqueue.models.enums.WeekType;
import com.project.groupqueue.repo.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
	private final LessonRepository lessonRepository;

	public void deleteAll() {
		lessonRepository.deleteAll();
	}

	public void changeSortType(GroupSchedule groupSchedule) {
		long lessonId = groupSchedule.getLessonId();
		SortType sortType = groupSchedule.getSortType();

		LessonEntity lessonEntity = lessonRepository.getLessonEntityById(lessonId);
		lessonEntity.setSortType(sortType);

		lessonRepository.save(lessonEntity);
	}

	public List<Lesson> getScheduleInfoByStudentIdGroupId(long studentId, long groupId) {
		return lessonRepository.getScheduleInfoByStudentIdGroupId(studentId, groupId);
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
