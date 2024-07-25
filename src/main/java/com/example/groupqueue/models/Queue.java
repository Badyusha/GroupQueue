package com.example.groupqueue.models;

import com.example.groupqueue.models.enums.SortType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "`queue`")
public class Queue {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "order", columnDefinition = "int")
	private Integer order;

	//	METHODS
}
