package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.Student;
import com.example.groupqueue.services.RegistrationService;
import com.example.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
	private final RegistrationService registrationService;

	@PostMapping(value = "/student/registration")
	public void registerStudent(HttpServletResponse response, @RequestBody Student student) {
		registrationService.registerGroup(student.getGroupNumber());
		registrationService.registerStudent(student);
		CookieUtil.addRequired(response, student);
	}
}
