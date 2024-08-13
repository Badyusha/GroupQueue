package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.ScheduleException;
import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.models.enums.WeekType;
import com.example.groupqueue.repo.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
	private final LessonRepository lessonRepository;

	public List<Object[]> getScheduleInfoByUserIdGroupId(long userId, long groupId) {
		return lessonRepository.getScheduleInfoByUserIdGroupId(userId, groupId);
	}

	public void addLessonByScheduleList(int week, List<ScheduleEntity> scheduleEntityList) {
		WeekType weekType = WeekType.getWeekTypeByNumber(week);

		if(scheduleEntityList == null || scheduleEntityList.isEmpty()) {
			throw new ScheduleException("scheduleEntityList is null or empty. Probably there is no lessons on the week");
		}

		for(ScheduleEntity scheduleEntity : scheduleEntityList) {
			lessonRepository.save(new LessonEntity(
					scheduleEntity.getId(),
					SortType.HIGHEST_LAB_SUM,
					getLessonDate(scheduleEntity, weekType)
			));
		}
	}

	private LocalDate getLessonDate(ScheduleEntity scheduleEntity, WeekType week) {
		int weeksToAdd = 0;
		if(week.equals(WeekType.getNextWeekType())) {
			++weeksToAdd;
		}

		LocalDate today = LocalDate.now();
		java.time.DayOfWeek targetDay = DayOfWeek.getJavaTimeDayOfWeek(scheduleEntity.getDayOfWeek());

		return today.with(targetDay).plusWeeks(weeksToAdd);
	}
}
