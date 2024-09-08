package com.project.groupqueue.models.entities;

import com.project.groupqueue.models.enums.PermissionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`permission`")
public class PermissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name", columnDefinition = "ENUM('SHOW_BECOME_GROUP_ADMIN_REQUESTS'," +
												"'BECOME_GROUP_ADMIN'," +
												"'CHOOSE_SORT_TYPE')")
	private PermissionType permissionType;
}
