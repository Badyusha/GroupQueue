package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.PreQueueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PreQueueRepository extends CrudRepository<PreQueueEntity, Long> {
	@Query(value = """
					SELECT preQueue
					FROM PreQueueEntity preQueue
					WHERE preQueue.userId = ?1 AND preQueue.lessonId = ?2
					""")
	PreQueueEntity getPreQueueEntityByUserIdLessonId(long userId, long groupId);

	@Query(value = """
					SELECT pq
					FROM PreQueueEntity pq
					WHERE pq.lessonId = ?1
					""")
	List<PreQueueEntity> getPreQueueEntityListByLessonId(long lessonId);
}
