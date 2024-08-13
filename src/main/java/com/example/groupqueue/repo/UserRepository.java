package com.example.groupqueue.repo;

import com.example.groupqueue.models.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	@Query(value = "SELECT user " +
			"FROM UserEntity user " +
			"WHERE user.username = ?1")
	UserEntity getUserByUsername(String username);

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
				"WHERE user.username = ?1")
	Long getIdByUsername(String username);
}
