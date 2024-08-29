package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	@Query(value = """
					SELECT user
					FROM UserEntity user
					WHERE user.username = ?1
				""")
	UserEntity getUserByUsername(String username);

	@Query(value = """
					SELECT u.roleId
					FROM UserEntity u
					WHERE u.id = ?1
					""")
	long getRoleIdByUserId(long userId);

	@Query(value = """
					SELECT count(user.id) as usernameExists
					FROM UserEntity user
					WHERE user.username = ?1
				""")
	Integer usernameCountByName(String username);

	default boolean isUsernameExist(String username) {
		return usernameCountByName(username) != 0;
	}

	@Query(value = """
					SELECT u.roleEntity.name
					FROM UserEntity u
					WHERE u.id = ?1
				""")
	String getRoleNameByUserId(long userId);

	default boolean isRoleMatchByUserIdRoleName(long userId, String roleType) {
		return getRoleNameByUserId(userId).equals(roleType);
	}

	@Query(value = """
			SELECT count(user.id) as userExists
			FROM UserEntity user
			WHERE (user.username = ?1 and user.password = ?2)
				""")
	Integer usersCountByUsernamePassword(String username, String password);

	default boolean isUserExistByUsernamePassword(String username, String password) {
		return usersCountByUsernamePassword(username, password) != 0;
	}

	@Query(value = """
					SELECT user.id
					FROM UserEntity user
					WHERE user.username = ?1
				""")
	Long getIdByUsername(String username);

	@Query(value = """
					SELECT user.username, user.firstName,
					user.lastName, role.name AS roleName,
					group.number AS groupNumber, user.password
					FROM UserEntity user
					INNER JOIN GroupEntity group
						ON group.id = user.groupId
					INNER JOIN RoleEntity role
						ON role.id = user.roleId
					WHERE user.id = ?1
				""")
	List<Object[]> getUserInfoByUserId(long userId);

	@Query(value = """
					SELECT user.password
					FROM UserEntity user
					WHERE user.id = ?1
				""")
	String getPasswordByUserId(long userId);

	@Query(value = """
					SELECT user
					FROM UserEntity user
					WHERE user.id = ?1
				""")
	UserEntity getUserEntityByUserId(long userId);

	@Query(value = """
					SELECT r.name
					FROM RoleEntity r
					JOIN UserEntity u
					ON u.roleId = r.id
					WHERE u.id = ?1
				""")
	String getUserRoleByUserId(long userId);
}
