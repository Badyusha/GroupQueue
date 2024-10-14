package com.project.groupqueue.services;

import com.project.groupqueue.exceptions.AuthorizationException;
import com.project.groupqueue.models.dto.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
	private final StudentService studentService;

	public void isStudentExist(Student student) {
		boolean isStudentExist = studentService.isStudentExistByUsernamePassword(student);
		if(!isStudentExist) {
			throw new AuthorizationException("Incorrect username or password\nFor user: " + student);
		}
	}
}
