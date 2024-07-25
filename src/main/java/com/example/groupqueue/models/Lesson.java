package com.example.groupqueue.models;

import com.example.groupqueue.models.enums.SortType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "`lesson`")
public class Lesson {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@Enumerated(EnumType.STRING)
	@Column(name = "sort_type", columnDefinition = "ENUM('SIMPLE','RANDOM','HIGHEST_LAB','HIGHEST_LAB_SUM')")
	private SortType sortType;

	@Column(name = "date", columnDefinition = "DATETIME")
	private LocalDateTime date;
}
