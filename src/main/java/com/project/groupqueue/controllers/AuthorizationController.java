package com.project.groupqueue.controllers;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.services.AuthorizationService;
import com.project.groupqueue.services.StudentService;
import com.project.groupqueue.utils.CookieUtil;
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
