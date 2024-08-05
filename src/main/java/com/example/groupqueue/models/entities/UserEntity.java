package com.example.groupqueue.models.entities;

import com.example.groupqueue.encryption.Encryption;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`user`")
public class UserEntity {
	//	FIELDS
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

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "username", columnDefinition = "varchar(65)")
	private String username;

	@Column(name = "first_name", columnDefinition = "varchar(65)")
	private String firstName;

	@Column(name = "last_name", columnDefinition = "varchar(65)")
	private String lastName;

	@Column(name = "password", columnDefinition = "varchar(65)")
	private String password;

	public UserEntity(Long groupId, Long roleId, String username,
					  String firstName, String lastName, String password)
	{
		this.groupId = groupId;
		this.roleId = roleId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = Encryption.hashData(password);
	}

//	METHODS
}
