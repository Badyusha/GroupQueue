package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.services.UserService;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/user/main_page")
	public String mainPage() {
		return "views/user/mainPage";
	}

	@ResponseBody
	@GetMapping("/user/username/{username}/exists")
	public boolean isUsernameExist(@PathVariable String username) {
		return userService.isUsernameExist(username);
	}

	@ResponseBody
	@GetMapping("/user/get_info")
	public User getUserInfo(HttpServletRequest request) {
		return userService.getUserInfo(request);
	}

	@PostMapping("/user/edit_profile")
	public void editProfile(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
		userService.editProfile(request, user);
		CookieUtils.addRequired(response, user);
	}
}
