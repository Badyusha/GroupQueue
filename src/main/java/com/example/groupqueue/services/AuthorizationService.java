package com.example.groupqueue.services;

import com.example.groupqueue.exceptions.AuthorizationException;
import com.example.groupqueue.models.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
	private final UserService userService;

	public void isUserExist(User user) {
		boolean isUserExist = userService.isUserExistByUsernamePassword(user);
		if(!isUserExist) {
			throw new AuthorizationException("Incorrect username or password\nFor user: " + user);
		}
	}
}
