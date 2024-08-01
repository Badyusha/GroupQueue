package com.example.groupqueue.services;

import com.example.groupqueue.encryption.Encryption;
import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.RoleRepository;
import com.example.groupqueue.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public UserService(UserRepository userRepository, GroupRepository groupRepository,
						RoleRepository roleRepository)
	{
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.roleRepository = roleRepository;
	}

	public boolean isUsernameExist(String username) {
		return userRepository.isUsernameExist(username);
	}

	public boolean isAuthorizeSuccess(String username, String password) {
		return userRepository.isUserExistByUsernamePassword(username, Encryption.hashData(password));
	}

	public Long getIdByUsername(String username) {
		return userRepository.getIdByUsername(username);
	}

	public boolean isRegistrationUser(User user) {
		Long groupId = groupRepository.getGroupIdByNumber(user.getGroupNumber());
		Long roleId = roleRepository.getRoleIdByName(RoleType.USER);
		try {
			userRepository.save(user.makeUserEntity(user, groupId, roleId));
		} catch(Exception e) {
			System.err.println("Exception while saving user: " + user);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
