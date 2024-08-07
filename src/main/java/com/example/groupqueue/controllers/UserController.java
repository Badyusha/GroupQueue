package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.services.CookieService;
import com.example.groupqueue.services.ScheduleService;
import com.example.groupqueue.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
	private final UserService userService;
	private final CookieService cookieService;
	private final ScheduleService scheduleService;

	@Autowired
	public UserController(UserService userService, CookieService cookieService, ScheduleService scheduleService) {
		this.userService = userService;
		this.cookieService = cookieService;
		this.scheduleService = scheduleService;
	}

	@GetMapping("/main_page")
	public String mainPage() {
		return "views/user/mainPage";
	}

	@ResponseBody
	@GetMapping("/username_exists/{username}")
	public boolean isUsernameExist(@PathVariable String username) {
		return userService.isUsernameExist(username);
	}

	@ResponseBody
	@PostMapping(value = "/registration")
	public boolean registerUser(HttpServletResponse response, @RequestBody User user) {
		boolean isRegistrationSuccess = userService.isRegistrationSuccess(user);

		return isRegistrationSuccess && cookieService.isAddRequiredCookiesSuccess(response, user);
	}

	@ResponseBody
	@PostMapping("authorize")
	public boolean authorizeUser(HttpServletResponse response, @RequestBody User user) {
		boolean isAuthorizeSuccess = userService.isAuthorizeSuccess(user.getUsername(), user.getPassword());

		return isAuthorizeSuccess && cookieService.isAddRequiredCookiesSuccess(response, user);
	}
}
