package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.User;
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

	@PostMapping(value = "/user/registration")
	public void registerUser(HttpServletResponse response, @RequestBody User user) {
		registrationService.registerGroup(user.getGroupNumber());
		registrationService.registerUser(user);
		CookieUtil.addRequired(response, user);
	}
}
