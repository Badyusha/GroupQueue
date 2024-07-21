package com.example.groupqueue.models;

import jakarta.persistence.*;

@Entity
@Table(name = "queue")
public class Queue {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@ManyToOne
	@JoinColumn(name = "sort_id", nullable = false)
	private SortType sortType;

	//	CONSTRUCTORS
	public Queue() {}

	public Queue(Long id, Subject subject, Client client, SortType sortType) {
		this.id = id;
		this.subject = subject;
		this.client = client;
		this.sortType = sortType;
	}


	//	METHODS
	//		SETTERS & GETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
}
