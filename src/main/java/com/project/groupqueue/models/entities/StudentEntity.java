package com.project.groupqueue.models.entities;

import com.project.groupqueue.utils.EncryptionUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`student`")
public class StudentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", insertable = false, updatable = false, nullable = false)
	private GroupEntity groupEntity;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", insertable = false, updatable = false, nullable = false)
	private RoleEntity roleEntity;

	@Column(name = "group_id", columnDefinition = "BIGINT")
	private Long groupId;

	@Column(name = "role_id", columnDefinition = "BIGINT")
	private Long roleId;

	@Column(name = "username", columnDefinition = "VARCHAR(65)")
	private String username;

	@Column(name = "first_name", columnDefinition = "VARCHAR(65)")
	private String firstName;

	@Column(name = "last_name", columnDefinition = "VARCHAR(65)")
	private String lastName;

	@Column(name = "password", columnDefinition = "VARCHAR(65)")
	private String password;


	public StudentEntity(Long groupId,
						 Long roleId,
						 String username,
						 String firstName,
						 String lastName,
						 String notEncryptedPassword) {
		this.groupId = groupId;
		this.roleId = roleId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = EncryptionUtil.hashData(notEncryptedPassword);
	}

	public StudentEntity(long id,
						 long groupId,
						 long roleId,
						 String username,
						 String firstName,
						 String lastName,
						 String encryptedPassword) {
		this.id = id;
		this.roleId = roleId;
		this.groupId = groupId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = encryptedPassword;
	}

}
