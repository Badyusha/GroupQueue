package com.example.groupqueue.controllers;

import com.example.groupqueue.services.UserService;
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
}
