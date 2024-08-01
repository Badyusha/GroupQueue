package com.example.groupqueue.controllers.start;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.services.CookieService;
import com.example.groupqueue.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/main_page")
	public String mainPage() {
		return "/views/user/userMainPage";
	}

	@ResponseBody
	@GetMapping("/username_exists/{username}")
	public boolean isUsernameExist(@PathVariable String username) {
		return userService.isUsernameExist(username);
	}

	@ResponseBody
	@PostMapping(value = "/registration")
	public boolean registerUser(HttpServletResponse response, @RequestBody User user)
	{
		boolean isRegistrationSuccess = userService.isRegistrationUser(user);
		boolean isCreateCookieSuccess = CookieService.createCookie(response, "userId",
			userService.getIdByUsername(user.getUsername()).toString());

		return isRegistrationSuccess && isCreateCookieSuccess;
	}

	@ResponseBody
	@PostMapping("authorize")
	public boolean authorizeUser(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
		boolean isAuthorizeSuccess = userService.isAuthorizeSuccess(user.getUsername(), user.getPassword());
		if(isAuthorizeSuccess) {
			CookieService.createCookie(response, "userId",
										userService.getIdByUsername(user.getUsername()).toString());
		}
		return isAuthorizeSuccess;
	}
}
