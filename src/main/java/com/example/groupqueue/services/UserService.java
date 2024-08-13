package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.repo.UserRepository;
import com.example.groupqueue.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;

	public void saveUser(User user) {
		long roleId = roleService.getRoleIdByType(RoleType.USER);
		userRepository.save(user.toUserEntity(roleId));
	}

	public boolean isUsernameExist(String username) {
		return userRepository.isUsernameExist(username);
	}

	public Long getUserIdByUsername(User user) {
		return userRepository.getIdByUsername(user.getUsername());
	}

	public boolean isUserExistByUsernamePassword(User user) {
		return userRepository.isUserExistByUsernamePassword(user.getUsername(),
															EncryptionUtils.hashData(user.getPassword()));
	}

	public void fillInUser(User user) {
		UserEntity userEntity = userRepository.getUserByUsername(user.getUsername());
		user.copyFromUserEntity(userEntity);
	}
}
