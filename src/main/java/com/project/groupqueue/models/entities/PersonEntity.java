package com.project.groupqueue.models.entities;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.utils.EncryptionUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`person`")
public class PersonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username", columnDefinition = "VARCHAR(65)")
	private String username;

	@Column(name = "first_name", columnDefinition = "VARCHAR(65)")
	private String firstName;

	@Column(name = "last_name", columnDefinition = "VARCHAR(65)")
	private String lastName;

	@Column(name = "password", columnDefinition = "VARCHAR(65)")
	private String password;

	public PersonEntity(Student student) {
		this.username = student.getUsername();
		this.firstName = student.getFirstName();
		this.lastName = student.getLastName();
		this.password = EncryptionUtil.hashData(student.getPassword());
	}
}
