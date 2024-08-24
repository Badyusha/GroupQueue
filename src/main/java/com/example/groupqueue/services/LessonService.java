package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.ScheduleException;
import com.example.groupqueue.models.dto.GroupSchedule;
import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.LessonRepository;
import jakarta.servlet.http.HttpServletRequest;
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

	public void changeSortType(GroupSchedule groupSchedule) {
		long lessonId = groupSchedule.getLessonId();
		SortType sortType = groupSchedule.getSortType();

		LessonEntity lessonEntity = lessonRepository.getLessonEntityById(lessonId);
		lessonEntity.setSortType(sortType);

		lessonRepository.save(lessonEntity);
	}

	public List<Lesson> getScheduleInfoByUserIdGroupId(long userId, long groupId) {
		return lessonRepository.getScheduleInfoByUserIdGroupId(userId, groupId);
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
