package com.example.groupqueue.repo;

import com.example.groupqueue.models.dto.GroupQueue;
import com.example.groupqueue.models.dto.QueueInfo;
import com.example.groupqueue.models.entities.QueueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QueueRepository extends CrudRepository<QueueEntity, Long> {
	@Query(value = "select queue " +
					"from QueueEntity queue " +
					"where queue.userEntity.id = ?1")
	List<QueueEntity> getQueueEntityListByUserId(long userId);

	@Query("SELECT new com.example.groupqueue.models.dto.QueueInfo(" +
			"s.subjectName, " +
			"s.subjectFullName, " +
			"l.date, " +
			"s.startTime, " +
			"l.sortType, " +
			"s.subgroupType, " +
			"pq.passingLabs, " +
			"q.order, " +
			"l.id) " +
			"FROM QueueEntity q " +
			"JOIN q.lessonEntity l " +
			"JOIN l.scheduleEntity s " +
			"LEFT JOIN PreQueueEntity pq ON q.lessonId = pq.lessonId AND q.userId = pq.userId " +
			"WHERE q.userId = ?1")
	List<QueueInfo> findQueueResults(long userId);

	@Query("SELECT new com.example.groupqueue.models.dto.QueueInfo(" +
			"s.subjectName, " +
			"s.subjectFullName, " +
			"l.date, " +
			"s.startTime, " +
			"l.sortType, " +
			"s.subgroupType, " +
			"pq.passingLabs, " +
			"-1, " +
			"l.id) " +
			"FROM PreQueueEntity pq " +
			"JOIN pq.lessonEntity l " +
			"JOIN l.scheduleEntity s " +
			"LEFT JOIN QueueEntity q ON pq.lessonId = q.lessonId AND pq.userId = q.userId " +
			"WHERE pq.userId = ?1 AND q.id IS NULL")
	List<QueueInfo> findPreQueueResults(long userId);

	@Query(value = "SELECT DISTINCT new com.example.groupqueue.models.dto.GroupQueue(" +
					"q.order, u.username, " +
					"u.lastName, u.firstName, " +
					"pq.passingLabs" +
					") " +
					"FROM QueueEntity q " +
					"JOIN UserEntity u " +
						"ON u.id = q.userId " +
					"JOIN PreQueueEntity pq " +
						"ON pq.lessonId = ?1 " +
					"WHERE q.lessonId = ?1 " +
					"ORDER BY q.order ASC")
	List<GroupQueue> getGroupQueueByLessonId(long lessonId);

}
