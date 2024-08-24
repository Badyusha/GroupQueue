package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.PermissionEntity;
import com.example.groupqueue.models.enums.PermissionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
	@Query("""
				SELECT count(1)
				FROM PermissionRoleEntity pr
				INNER JOIN RoleEntity r
				ON r.id = pr.roleId
				INNER JOIN UserEntity u
				ON u.roleId = r.id
				INNER JOIN PermissionEntity p
				ON p.id = pr.permissionId
				WHERE p.permissionType = ?1 and u.roleId = ?2
			""")
	int getPermissionCountByPermissionNameRoleId(PermissionType permission, long roleId);

	default boolean isActionAllowed(PermissionType permission, long roleId) {
		return getPermissionCountByPermissionNameRoleId(permission, roleId) == 1;
	}
}
