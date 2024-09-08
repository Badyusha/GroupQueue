package com.project.groupqueue.repo;

import com.project.groupqueue.models.entities.PermissionEntity;
import com.project.groupqueue.models.enums.PermissionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
	@Query("""
				SELECT count(1)
				FROM PermissionRoleEntity pr
				INNER JOIN RoleEntity r
				ON r.id = pr.roleId
				INNER JOIN StudentEntity s
				ON s.roleId = r.id
				INNER JOIN PermissionEntity p
				ON p.id = pr.permissionId
				WHERE p.permissionType = ?1 and s.roleId = ?2
			""")
	int getPermissionCountByPermissionNameRoleId(PermissionType permission, long roleId);

	default boolean isActionAllowed(PermissionType permission, long roleId) {
		return getPermissionCountByPermissionNameRoleId(permission, roleId) != 1;
	}
}
