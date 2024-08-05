package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.GroupEntity;
import com.example.groupqueue.models.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	@Query(value = "SELECT count(user.id) as usernameExists " +
			"FROM UserEntity user " +
			"WHERE user.username = ?1")
	Integer usernameCountByName(String username);

	default boolean isUsernameExist(String username) {
		return usernameCountByName(username) != 0;
	}

	@Query(value = "SELECT count(user.id) as userExists " +
			"FROM UserEntity user " +
			"WHERE (user.username = ?1 and user.password = ?2)")
	Integer usersCountByUsernamePassword(String username, String password);

	default boolean isUserExistByUsernamePassword(String username, String password) {
		return usersCountByUsernamePassword(username, password) != 0;
	}

	@Query(value = "SELECT user.id " +
			"FROM UserEntity user " +
			"where user.username = ?1")
	Long getIdByUsername(String username);

	@Modifying
	@Transactional
	@Query(value = "insert into `user`(group_id, role_id, username, first_name, last_name, password)" +
			"values (:groupId, :roleId, :username, :firstName, :lastName, :password);", nativeQuery = true)
	void saveUser(@Param("groupId") Long groupId, @Param("roleId") Long roleId,
				  @Param("username") String username, @Param("firstName") String firstName,
				  @Param("lastName") String lastName, @Param("password") String password);
}
