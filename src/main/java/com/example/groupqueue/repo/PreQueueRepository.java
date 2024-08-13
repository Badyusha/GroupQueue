package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.PreQueueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PreQueueRepository extends CrudRepository<PreQueueEntity, Long> {
	@Query(value = "select preQueue " +
			"from PreQueueEntity preQueue " +
			"where preQueue.userEntity.id = ?1")
	List<PreQueueEntity> getPreQueueEntityListByUserId(long userId);
}
