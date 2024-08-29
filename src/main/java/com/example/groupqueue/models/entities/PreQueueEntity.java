package com.example.groupqueue.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`pre_queue`")
public class PreQueueEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lesson_id", insertable = false, updatable = false, nullable = false)
	private LessonEntity lessonEntity;

	@Column(name = "lesson_id", columnDefinition = "BIGINT")
	private Long lessonId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
	private UserEntity userEntity;

	@Column(name = "user_id", columnDefinition = "BIGINT")
	private Long userId;

	@Column(name = "passing_labs", columnDefinition = "BLOB")
	private byte[] passingLabs;

	@Column(name = "registration_time", columnDefinition = "TIME")
	private LocalTime registrationTime;


	public PreQueueEntity(long lessonId, long userId, byte[] passingLabs) {
		this.lessonId = lessonId;
		this.userId = userId;
		this.passingLabs = passingLabs;
		this.registrationTime = LocalTime.now();
	}

	public QueueEntity toQueueEntity(int order) {
		return new QueueEntity(lessonId, userId, order);
	}
}
