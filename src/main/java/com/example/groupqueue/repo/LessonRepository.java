package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.enums.WeekType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LessonRepository extends CrudRepository<LessonEntity, Long> {
	@Query(value = "select lesson " +
			"from LessonEntity lesson " +
			"where lesson.scheduleEntity.id = ?1")
	LessonEntity getLessonEntityByScheduleId(long scheduleId);

	@Query(value = 	"SELECT lesson.id AS lessonId, schedule.subjectName, " +
			"schedule.subjectFullName, schedule.dayOfWeek, " +
			"schedule.subgroupType, lesson.date, schedule.weekType, " +
			"schedule.startTime, queue.order AS numberInQueue, queue.id AS queueId, " +
			"CASE " +
				"WHEN preQueue.id IS NOT NULL THEN TRUE " +
				"ELSE FALSE " +
			"END AS is_registered_in_queue " +
			"FROM LessonEntity lesson " +
			"INNER JOIN ScheduleEntity schedule " +
				"ON schedule.id = lesson.scheduleId " +
			"LEFT JOIN PreQueueEntity preQueue " +
				"ON preQueue.lessonId = lesson.id " +
			"LEFT JOIN QueueEntity queue " +
				"ON queue.lessonId = lesson.id AND queue.userId = ?1 " +
			"WHERE schedule.groupId = ?2 " +
			"ORDER BY " +
				"schedule.startTime ASC")
	List<Object[]> getScheduleInfoByUserIdGroupId(long userId, long groupId);
}
