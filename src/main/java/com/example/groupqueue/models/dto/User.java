package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.services.GroupService;
import com.example.groupqueue.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
	private Long userId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Long groupId;
	private Integer groupNumber;

	public UserEntity toUserEntity(Long roleId) {
		return new UserEntity(groupId, roleId, username, firstName, lastName, password);
	}

	public void copyFromUserEntity(UserEntity userEntity) {
		this.userId = userEntity.getId();
		this.firstName = userEntity.getFirstName();
		this.lastName = userEntity.getLastName();
		this.username = userEntity.getUsername();
		this.password = userEntity.getPassword();
		this.groupId = userEntity.getGroupId();
		this.groupNumber = userEntity.getGroupEntity().getNumber();
	}
}
