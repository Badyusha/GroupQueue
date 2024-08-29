package com.example.groupqueue.models.entities;

import com.example.groupqueue.models.enums.SortType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`lesson`")
public class LessonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedule_id", insertable = false, updatable = false, nullable = false)
	private ScheduleEntity scheduleEntity;

	@Column(name = "schedule_id", columnDefinition = "BIGINT")
	private Long scheduleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "sort_type", columnDefinition = "ENUM('SIMPLE','RANDOM','HIGHEST_LAB','HIGHEST_LAB_SUM')")
	private SortType sortType;

	@Column(name = "date", columnDefinition = "DATETIME")
	private LocalDate date;


	public LessonEntity(Long scheduleId, SortType sortType, LocalDate date) {
		this.scheduleId = scheduleId;
		this.sortType = sortType;
		this.date = date;
	}
}
