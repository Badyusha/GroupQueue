package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.GroupEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
	@Modifying
	@Transactional
	@Query(value = "insert into `group`(number) " +
			"values(:groupNumber)", nativeQuery = true)
	void saveGroup(@Param("groupNumber") Integer groupNumber);


	@Query(value = "SELECT user.groupEntity.id " +
			"FROM UserEntity user " +
			"WHERE user.id = ?1")
	Long getGroupIdByUserId(Long userId);

	@Query(value = "SELECT user.groupEntity.number " +
			"FROM UserEntity user " +
			"WHERE user.id = ?1")
	Integer getGroupNumberByUserId(Long userId);

	@Query(value = "SELECT group.number " +
			"FROM GroupEntity group " +
			"WHERE group.id = ?1")
	Integer getGroupNumberById(Long groupId);

	@Query(value = "SELECT group.id as group_id " +
			"FROM GroupEntity group " +
			"WHERE group.number = ?1")
	Long getGroupIdByNumber(Integer groupNumber);

	default boolean isGroupInDb(Integer groupNumber) {
		return getGroupIdByNumber(groupNumber) != null;
	}
}
