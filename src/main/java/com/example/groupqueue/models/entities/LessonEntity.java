package com.example.groupqueue.models.entities;

import com.example.groupqueue.models.enums.SortType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`lesson`")
public class LessonEntity {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "schedule_id", nullable = false)
	private ScheduleEntity scheduleEntity;

	@Enumerated(EnumType.STRING)
	@Column(name = "sort_type", columnDefinition = "ENUM('SIMPLE','RANDOM','HIGHEST_LAB','HIGHEST_LAB_SUM')")
	private SortType sortType;

	@Column(name = "date", columnDefinition = "DATETIME")
	private LocalDateTime date;
}
