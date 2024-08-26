package com.example.groupqueue.services;

import com.example.groupqueue.external.api.BsuirAPI;
import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.repo.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final UserService userService;
	private final GroupRepository groupRepository;
	private final ScheduleService scheduleService;

	public void registerUser(User user) {
		long groupId = groupRepository.getGroupIdByNumber(user.getGroupNumber());
		user.setGroupId(groupId);
		userService.saveUser(user);
		long userId = userService.getUserIdByUser(user);
		user.setUserId(userId);
	}

	public void registerGroup(int groupNumber) {
		if(!groupRepository.isGroupExist(groupNumber) && BsuirAPI.isGroupExist(groupNumber)) {
			groupRepository.saveGroup(groupNumber);
			scheduleService.addRecordsForNewGroupByGroupNumber(groupNumber);
		}
	}
}
