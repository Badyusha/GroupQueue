package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.repo.UserRepository;
import com.example.groupqueue.utils.CookieUtils;
import com.example.groupqueue.utils.EncryptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final GroupService groupService;

	public void saveUser(User user) {
		long roleId = roleService.getRoleIdByType(RoleType.USER);
		user.setRoleId(roleId);
		userRepository.save(user.toUserEntity());
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

	public User getUserInfo(HttpServletRequest request) {
		long userId = CookieUtils.getUserId(request);

		List<Object[]> userList = userRepository.getUserInfoByUserId(userId);
		Object[] user = userList.getFirst();

		String username = (String) user[0];
		String firstName = (String) user[1];
		String lastName = (String) user[2];
		RoleType roleType = (RoleType) user[3];
		Integer groupNumber = (Integer) user[4];
		String password = (String) user[5];

		return new User(
				null,
				firstName,
				lastName,
				username,
				password,
				null,
				groupNumber,
				roleType
		);
	}

	public void editProfile(HttpServletRequest request, User user) {
		long userId = CookieUtils.getUserId(request);
		UserEntity userEntity = userRepository.getUserEntityByUserId(userId);
		long roleId = userEntity.getRoleId();

		user.setUserId(userId);
		user.setRoleId(roleId);
		user.setGroupId(groupService.getGroupIdByNumber(user.getGroupNumber()));
		if(!user.getPassword().isEmpty()) {
			userRepository.save(user.toUserEntityWithPasswordEncryption());
			return;
		}

		user.setPassword(userEntity.getPassword());
		userRepository.save(user.toUserEntityWithoutPasswordEncryption());
	}
}
