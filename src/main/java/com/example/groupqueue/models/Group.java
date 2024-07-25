package com.example.groupqueue.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "`group`")
public class Group {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "number", columnDefinition = "int")
	private Integer number;
}
