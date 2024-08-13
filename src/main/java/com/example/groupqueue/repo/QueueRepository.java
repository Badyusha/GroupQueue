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
}
