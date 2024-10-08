package com.project.groupqueue.repo;

import com.project.groupqueue.models.entities.RoleEntity;
import com.project.groupqueue.models.enums.RoleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	@Query(value = """
					SELECT role.id
					FROM RoleEntity role
					WHERE role.name = ?1
				""")
	Long getRoleIdByType(RoleType roleType);
}
