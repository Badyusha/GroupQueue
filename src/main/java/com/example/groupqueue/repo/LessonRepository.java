package com.example.groupqueue.repo;

import com.example.groupqueue.models.dto.Lesson;
import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.enums.WeekType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LessonRepository extends CrudRepository<LessonEntity, Long> {
	@Query(value = "select lesson " +
			"from LessonEntity lesson " +
			"where lesson.id = ?1")
	LessonEntity getLessonEntityById(long scheduleId);

	@Query("SELECT new com.example.groupqueue.models.dto.Lesson" +
			"(" +
			"lesson.id, " +
			"schedule.subjectName, " +
			"schedule.subjectFullName, " +
			"schedule.subgroupType, " +
			"schedule.startTime, " +
			"CASE " +
				"WHEN preQueue.id IS NOT NULL " +
					"THEN TRUE " +
					"ELSE FALSE " +
			"END, " +
			"queue.order, " +
			"queue.id, " +
			"schedule.dayOfWeek, " +
			"lesson.date, " +
			"schedule.weekType, " +
			"CASE " +
				"WHEN preQueue.id IS NOT NULL " +
				"THEN TRUE " +
				"ELSE FALSE " +
			"END" +
			") " +
			"FROM LessonEntity lesson " +
			"INNER JOIN ScheduleEntity schedule " +
				"ON schedule.id = lesson.scheduleId " +
			"LEFT JOIN PreQueueEntity preQueue " +
				"ON preQueue.lessonId = lesson.id AND preQueue.userId = ?1 " +
			"LEFT JOIN QueueEntity queue " +
				"ON queue.lessonId = lesson.id AND queue.userId = ?1 " +
			"WHERE schedule.groupId = ?2 " +
			"ORDER BY schedule.startTime ASC")
	List<Lesson> getScheduleInfoByUserIdGroupId(long userId, long groupId);
}
