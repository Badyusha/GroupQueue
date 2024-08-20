package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final UserService userService;
	private final GroupService groupService;

	public void registerUser(User user) {
		long groupId = groupService.getGroupIdByNumber(user.getGroupNumber());
		user.setGroupId(groupId);
		userService.saveUser(user);
		long userId = userService.getUserIdByUser(user);
		user.setUserId(userId);
	}
}
