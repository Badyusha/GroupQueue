package com.example.groupqueue.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
	//	FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "username", columnDefinition = "varchar(20)")
	private String username;

	@Column(name = "password", columnDefinition = "varchar(65)")
	private String password;

	@Column(name = "role", columnDefinition = "ENUM('client','admin')")
	private String role;

	public enum Role {
		client,
		admin
	}

	//	CONSTRUCTORS
	public User() {}

	public User(Long id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}


	//	METHODS
	//		SETTERS & GETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	//		OVERRIDE
	@Override
	public String toString() {
		return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\'' + ", role='" + Role.valueOf(role) + '\'' + '}';
	}
}
