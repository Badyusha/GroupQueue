package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.GroupEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
	@Query(value = "SELECT group.id as group_id " +
			"FROM GroupEntity group " +
			"WHERE group.number = ?1")
	Long getGroupIdByNumber(Integer groupNumber);

	default boolean isGroupInDb(Integer groupNumber) {
		return getGroupIdByNumber(groupNumber) != null;
	}

	@Modifying
	@Transactional
	@Query(value = "insert into `group`(number) " +
			"values(:groupNumber)", nativeQuery = true)
	void saveGroup(@Param("groupNumber") Integer groupNumber);
}
