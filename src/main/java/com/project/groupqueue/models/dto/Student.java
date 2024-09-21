package com.project.groupqueue.models.dto;

import com.project.groupqueue.models.entities.StudentEntity;
import com.project.groupqueue.models.enums.RoleType;
import lombok.Data;

@Data
public class Student {
	private Long studentId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Long groupId;
	private Long personId;
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

	public StudentEntity toStudentEntity(Long personId) {
		return new StudentEntity(groupId, roleId, personId);
	}

	public void copyFromStudentEntity(StudentEntity studentEntity) {
		this.studentId = studentEntity.getId();
		this.firstName = studentEntity.getPersonEntity().getFirstName();
		this.lastName = studentEntity.getPersonEntity().getLastName();
		this.username = studentEntity.getPersonEntity().getUsername();
		this.password = studentEntity.getPersonEntity().getPassword();
		this.groupId = studentEntity.getGroupId();
		this.groupNumber = studentEntity.getGroupEntity().getNumber();
	}
}
