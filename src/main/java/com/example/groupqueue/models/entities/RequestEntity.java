package com.example.groupqueue.models.entities;

import com.example.groupqueue.models.enums.RequestType;
import com.example.groupqueue.models.enums.SubgroupType;
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
	@JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
	private UserEntity user;

	@Column(name = "user_id")
	private Long userId;

	public RequestEntity(RequestType requestType, long userId) {
		this.requestType = requestType;
		this.userId = userId;
	}
}
