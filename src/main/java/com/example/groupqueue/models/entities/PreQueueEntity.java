package com.example.groupqueue.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`pre_queue`")
public class PreQueueEntity {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lesson_id", insertable = false, updatable = false, nullable = false)
	private LessonEntity lessonEntity;

	@Column(name = "lesson_id")
	private Long lessonId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
	private UserEntity userEntity;

	@Column(name = "user_id")
	private Long userId;
}
