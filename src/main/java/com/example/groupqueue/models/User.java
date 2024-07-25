package com.example.groupqueue.models;

import com.example.groupqueue.models.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "`user`")
public class User {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "group_id", nullable = false)
	private Group group;

	@Column(name = "first_name", columnDefinition = "varchar(65)")
	private String first_name;

	@Column(name = "last_name", columnDefinition = "varchar(65)")
	private String last_name;

	@Column(name = "password", columnDefinition = "varchar(65)")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", columnDefinition = "ENUM('CLIENT','ADMIN')")
	private RoleType role;

	//	METHODS
}
