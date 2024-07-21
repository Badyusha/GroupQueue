package com.example.groupqueue.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subject")
public class Subject {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", columnDefinition = "varchar(30)")
	private String name;

	@Column(name = "group_number", columnDefinition = "int")
	private Integer groupNumber;

	@Column(name = "subgroup", columnDefinition = "ENUM('first','second','all')")
	private String subgroup;

	@Column(name = "date", columnDefinition = "DATETIME")
	private LocalDateTime date;

	public enum Subgroup {
		first,
		second,
		all

	}

	//	CONSTRUCTORS
	public Subject() {}

	public Subject(Long id, String name, Integer groupNumber, String subgroup, LocalDateTime date) {
		this.id = id;
		this.name = name;
		this.groupNumber = groupNumber;
		this.subgroup = subgroup;
		this.date = date;
	}


	//	METHODS
	//		SETTERS & GETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	//		OVERRIDE
	@Override
	public String toString() {
		return "Subject{" + "id=" + id + ", name='" + name + '\'' + ", groupNumber=" + groupNumber + ", subgroup='" + Subgroup.valueOf(subgroup) + '\'' + ", date=" + date + '}';
	}
}
