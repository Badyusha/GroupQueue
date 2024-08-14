package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.utils.EncryptionUtils;
import lombok.Data;

@Data
public class User {
	private Long userId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Long groupId;
	private Integer groupNumber;
	private RoleType roleType;
	private Long roleId;

	public User(Long userId,
				String firstName,
				String lastName,
				String username,
				String password,
				Long groupId,
				Integer groupNumber,
				RoleType roleType) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.groupId = groupId;
		this.groupNumber = groupNumber;
		this.roleType = roleType;
	}

	public UserEntity toUserEntity() {
		return new UserEntity(groupId, roleId, username, firstName, lastName, password);
	}

	public UserEntity toUserEntityWithPasswordEncryption() {
		return new UserEntity(userId, groupId, roleId, username, firstName, lastName, EncryptionUtils.hashData(password));
	}

	public UserEntity toUserEntityWithOutPasswordEncryption() {
		return new UserEntity(userId, groupId, roleId, username, firstName, lastName, password);
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
