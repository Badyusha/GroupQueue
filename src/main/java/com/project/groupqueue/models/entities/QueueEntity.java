package com.project.groupqueue.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`queue`")
public class QueueEntity {
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
	@JoinColumn(name = "student_id", insertable = false, updatable = false, nullable = false)
	private StudentEntity studentEntity;

	@Column(name = "student_id", columnDefinition = "BIGINT")
	private Long studentId;

	@Column(name = "`order`", columnDefinition = "INT")
	private Integer order;


	public QueueEntity(long lessonId, long studentId, int order) {
		this.lessonId = lessonId;
		this.studentId = studentId;
		this.order = order;
	}
}
