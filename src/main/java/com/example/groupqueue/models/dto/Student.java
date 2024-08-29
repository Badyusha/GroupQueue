package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.entities.StudentEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.utils.EncryptionUtil;
import lombok.Data;

@Data
public class Student {
	private Long studentId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Long groupId;
	private Integer groupNumber;
	private RoleType roleType;
	private Long roleId;

	public Student(Long studentId,
				   String firstName,
				   String lastName,
				   String username,
				   String password,
				   Long groupId,
				   Integer groupNumber,
				   RoleType roleType) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.groupId = groupId;
		this.groupNumber = groupNumber;
		this.roleType = roleType;
	}

	public StudentEntity toStudentEntity() {
		return new StudentEntity(groupId, roleId, username, firstName, lastName, password);
	}

	public StudentEntity toStudentEntityWithPasswordEncryption() {
		return new StudentEntity(studentId, groupId, roleId, username, firstName, lastName, EncryptionUtil.hashData(password));
	}

	public StudentEntity toStudentEntityWithOutPasswordEncryption() {
		return new StudentEntity(studentId, groupId, roleId, username, firstName, lastName, password);
	}

	public void copyFromStudentEntity(StudentEntity studentEntity) {
		this.studentId = studentEntity.getId();
		this.firstName = studentEntity.getFirstName();
		this.lastName = studentEntity.getLastName();
		this.username = studentEntity.getUsername();
		this.password = studentEntity.getPassword();
		this.groupId = studentEntity.getGroupId();
		this.groupNumber = studentEntity.getGroupEntity().getNumber();
	}
}
