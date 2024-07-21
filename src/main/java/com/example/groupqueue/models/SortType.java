package com.example.groupqueue.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sort_type")
public class SortType {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "type", columnDefinition = "ENUM('simple','random','highest_lab','highest_lab_sum')")
	private String type;

	public enum Type {
		simple,
		random,
		highest_lab,
		highest_lab_sum;
	}

	//	CONSTRUCTORS
	public SortType() {}

	public SortType(Long id, String type) {
		this.id = id;
		this.type = type;
	}


	//	METHODS
	//		SETTERS & GETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//		OVERRIDE
	@Override
	public String toString() {
		return "SortType{" + "id=" + id + ", type='" + Type.valueOf(type) + '\'' + '}';
	}
}
