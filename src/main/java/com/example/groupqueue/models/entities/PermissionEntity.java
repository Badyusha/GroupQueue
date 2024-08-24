package com.example.groupqueue.models.entities;

import com.example.groupqueue.models.enums.PermissionType;
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
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name", columnDefinition = "ENUM('BECOME_GROUP_ADMIN','CHOOSE_SORT_TYPE')")
	private PermissionType permissionType;
}
