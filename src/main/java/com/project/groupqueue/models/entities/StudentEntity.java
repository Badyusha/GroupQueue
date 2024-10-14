package com.project.groupqueue.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
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

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", insertable = false, updatable = false, nullable = false)
	private PersonEntity personEntity;



	@Column(name = "group_id", columnDefinition = "BIGINT")
	private Long groupId;

	@Column(name = "role_id", columnDefinition = "BIGINT")
	private Long roleId;

	@Column(name = "person_id", columnDefinition = "BIGINT")
	private Long personId;


	public StudentEntity(Long groupId,
						 Long roleId,
						 Long personId) {
		this.groupId = groupId;
		this.roleId = roleId;
		this.personId = personId;
	}

	public StudentEntity(long id,
						 long groupId,
						 long roleId,
						 long personId) {
		this.id = id;
		this.roleId = roleId;
		this.groupId = groupId;
		this.personId = personId;
	}

}
