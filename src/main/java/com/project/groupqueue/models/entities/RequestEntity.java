package com.project.groupqueue.models.entities;

import com.project.groupqueue.models.enums.RequestType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`request`")
public class RequestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "request_type", columnDefinition = "ENUM('BECOME_GROUP_ADMIN')")
	private RequestType requestType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id", insertable = false, updatable = false, nullable = false)
	private StudentEntity studentEntity;

	@Column(name = "student_id", columnDefinition = "BIGINT")
	private Long studentId;

	public RequestEntity(RequestType requestType, long studentId) {
		this.requestType = requestType;
		this.studentId = studentId;
	}
}
