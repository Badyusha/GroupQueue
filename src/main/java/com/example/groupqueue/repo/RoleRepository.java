package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.RoleEntity;
import com.example.groupqueue.models.enums.RoleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	@Query(value = "SELECT role.id as role_id " +
			"FROM RoleEntity role " +
			"WHERE role.name = ?1")
	Long getRoleIdByName(RoleType roleType);
}
