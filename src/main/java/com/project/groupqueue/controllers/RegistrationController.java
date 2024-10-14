package com.project.groupqueue.controllers;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.services.RegistrationService;
import com.project.groupqueue.utils.CookieUtil;
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
