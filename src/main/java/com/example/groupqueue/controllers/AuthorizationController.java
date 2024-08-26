package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.services.AuthorizationService;
import com.example.groupqueue.services.UserService;
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
	private final UserService userService;

	@ResponseBody
	@PostMapping(value = "/user/authorization")
	public void authorizeUser(HttpServletResponse response, @RequestBody User user) {
		authorizationService.isUserExist(user);
		userService.fillInUser(user);
		CookieUtil.addRequired(response, user);
	}
}
