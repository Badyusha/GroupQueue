package com.example.groupqueue.models;

import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.models.enums.SubgroupType;
import com.example.groupqueue.models.enums.WeekType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "`subject`")
public class Schedule {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "subject_name", columnDefinition = "varchar(30)")
	private String subjectName;

	@Enumerated(EnumType.STRING)
	@Column(name = "subgroup_type", columnDefinition = "ENUM('FIRST','SECOND','ALL')")
	private SubgroupType subgroupType;

	@Column(name = "start_time", columnDefinition = "DATETIME")
	private LocalTime startTime;

	@ManyToOne
	@JoinColumn(name = "group_id", nullable = false)
	private Group group;

	@Enumerated(EnumType.STRING)
	@Column(name = "week_type", columnDefinition = "ENUM('FIRST','SECOND','THIRD','FOURTH')")
	private WeekType weekType;

	@Enumerated(EnumType.STRING)
	@Column(name = "day_of_week", columnDefinition = "ENUM('MONDAY','TUESDAY','WEDNESDAY'," +
													"'THURSDAY','FRIDAY','SATURDAY','SUNDAY')")
	private DayOfWeek dayOfWeek;

	//	METHODS
}
