package com.example.groupqueue.models.entities;

import com.example.groupqueue.models.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`role`")
public class RoleEntity {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name", columnDefinition = "ENUM('USER','GROUP_ADMIN','SUDO')")
	private RoleType name;
}
