package com.project.groupqueue.repo;

import com.project.groupqueue.models.entities.GroupEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
	@Query(value = """
					SELECT group.id as group_id
					FROM GroupEntity group
					WHERE group.number = ?1
				""")
	Long getGroupIdByNumber(Integer groupNumber);

	default boolean isGroupExist(Integer groupNumber) {
		return getGroupIdByNumber(groupNumber) != null;
	}
}
