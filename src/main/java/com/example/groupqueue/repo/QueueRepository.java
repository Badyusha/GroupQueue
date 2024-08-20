package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.QueueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QueueRepository extends CrudRepository<QueueEntity, Long> {
	@Query(value = "select queue " +
			"from QueueEntity queue " +
			"where queue.userEntity.id = ?1")
	List<QueueEntity> getQueueEntityListByUserId(long userId);

	@Query(value = "SELECT schedule.subjectName, schedule.subjectFullName, " +
			"lesson.date, schedule.startTime, " +
			"schedule.subgroupType, preQueue.passingLabs, " +
			"COALESCE(queue.order, NULL) AS numberInQueue, " +
			"CASE " +
				"WHEN queue.order IS NOT NULL THEN (" +
					"SELECT COUNT(1) " +
					"FROM QueueEntity queue " +
					"WHERE queue.lessonId = preQueue.lessonId" +
				") " +
				"ELSE ( " +
					"SELECT COUNT(1) " +
					"FROM PreQueueEntity preQueue " +
					"WHERE preQueue.lessonId = lesson.id " +
				") " +
			"END AS totalStudentsNumber, " +
			"queue.id AS queueId " +
			"FROM PreQueueEntity preQueue " +
			"LEFT JOIN LessonEntity lesson " +
				"ON lesson.id = preQueue.lessonId " +
			"INNER JOIN ScheduleEntity schedule " +
				"ON schedule.id = lesson.scheduleId " +
			"LEFT JOIN QueueEntity queue " +
				"ON queue.lessonId = preQueue.lessonId AND queue.userId = ?1 " +
			"WHERE schedule.groupId = ?2 " +
			"GROUP BY " +
				"schedule.subjectName, " +
				"schedule.subjectFullName, " +
				"schedule.startTime, " +
				"lesson.date, " +
				"schedule.subgroupType, " +
				"preQueue.passingLabs, " +
				"queue.order, " +
				"queue.id, " +
				"preQueue.lessonId")
	List<Object[]> getQueueInfoByUserIdGroupId(long userId, long groupId);
}
