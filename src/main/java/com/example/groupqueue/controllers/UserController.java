package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.services.UserService;
import com.example.groupqueue.utils.CookieUtil;
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
	public String mainPage(HttpServletRequest request) {
		if(!CookieUtil.isCookiesExists(request)) {
			return "redirect:/";
		}
		return "views/user/mainPage";
	}

	@GetMapping("/user/queues")
	public String getQueues(HttpServletRequest request) {
		if(!CookieUtil.isCookiesExists(request)) {
			return "redirect:/";
		}
		return "views/user/userQueues";
	}

	@GetMapping("/user/username/{username}/exists")
	@ResponseBody
	public boolean isUsernameExist(@PathVariable String username) {
		return userService.isUsernameExist(username);
	}

	@GetMapping("/user/{roleType}/is_user_role")
	@ResponseBody
	public boolean isItUserRoleType(HttpServletRequest request, @PathVariable String roleType) {
		return userService.isItUserRole(request, roleType);
	}

	@GetMapping("/user/password/{password}/matches")
	@ResponseBody
	public boolean isPasswordMatches(HttpServletRequest request, @PathVariable String password) {
		return userService.isPasswordMatches(request, password);
	}

	@GetMapping("/user/get/info")
	@ResponseBody
	public User getUserInfo(HttpServletRequest request) {
		return userService.getUserInfo(request);
	}

	@PostMapping("/user/edit_profile")
	public void editProfile(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
		userService.editProfile(request, user);
		CookieUtil.addRequired(response, user);
	}

	@DeleteMapping("/user/delete")
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
		userService.deleteUserByUserId(request);
		CookieUtil.deleteAllCookies(response, request);
	}

	@GetMapping("/user/get/role")
	@ResponseBody
	public String getUserRole(HttpServletRequest request) {
		return userService.getUserRoleByUserId(request);
	}
}
