package com.example.groupqueue.models;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
public class Client {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "BIGINT")
	private User user;

	@Column(name = "first_name", columnDefinition = "varchar(25)")
	private String firstName;

	@Column(name = "last_name", columnDefinition = "varchar(25)")
	private String lastName;

	@Column(name = "group_number", columnDefinition = "int")
	private Integer groupNumber;

	@Column(name = "email", columnDefinition = "varchar(50)")
	private String email;

	//	CONSTRUCTORS
	public Client() {}

	public Client(Long id, User user, String firstName, String lastName, Integer groupNumber, String email) {
		this.id = id;
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.groupNumber = groupNumber;
		this.email = email;
	}

	//	METHODS
	//		SETTERS & GETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//		OVERRIDE
	@Override
	public String toString() {
		return "Client{" + "id=" + id + ", user=" + user + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", groupNumber=" + groupNumber + ", email='" + email + '\'' + '}';
	}
}
