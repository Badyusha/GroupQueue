package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.Student;
import com.example.groupqueue.services.AuthorizationService;
import com.example.groupqueue.services.StudentService;
import com.example.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {
	private final AuthorizationService authorizationService;
	private final StudentService studentService;

	@ResponseBody
	@PostMapping(value = "/student/authorization")
	public void authorizeStudent(HttpServletResponse response, @RequestBody Student student) {
		authorizationService.isStudentExist(student);
		studentService.fillInStudent(student);
		CookieUtil.addRequired(response, student);
	}
}
