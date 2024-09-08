package com.project.groupqueue.models.entities;

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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "permission_id", insertable = false, updatable = false, nullable = false)
	private PermissionEntity permissionEntity;

	@Column(name = "permission_id", columnDefinition = "BIGINT", updatable = false)
	private Long permissionId;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "role_id", insertable = false, updatable = false, nullable = false)
	private RoleEntity roleEntity;

	@Column(name = "role_id", columnDefinition = "BIGINT", updatable = false)
	private Long roleId;
}
