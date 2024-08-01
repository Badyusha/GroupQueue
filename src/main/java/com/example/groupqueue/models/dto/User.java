package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.UserEntity;
import lombok.Data;

@Data
public class User {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Integer groupNumber;

	public UserEntity makeUserEntity(User user, Long groupId, Long roleId) {
		return new UserEntity(groupId, roleId, user.username,
							user.firstName, user.lastName, user.password);
	}
}
