package com.example.groupqueue.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`permission_role`")
public class PermissionRoleEntity {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "permission_id", nullable = false)
	private PermissionEntity permissionEntity;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	private RoleEntity roleEntity;
}
