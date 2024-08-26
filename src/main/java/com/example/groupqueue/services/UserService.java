package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.User;
import com.example.groupqueue.models.entities.UserEntity;
import com.example.groupqueue.models.enums.RoleType;
import com.example.groupqueue.repo.UserRepository;
import com.example.groupqueue.utils.CookieUtil;
import com.example.groupqueue.utils.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;

	public long getRoleIdByUserId(long userId) {
		return userRepository.getRoleIdByUserId(userId);
	}

	public void updateUserRoleType(long userId) {
		UserEntity user = userRepository.getUserEntityByUserId(userId);
		long roleId = roleService.getRoleIdByType(RoleType.GROUP_ADMIN);
		user.setRoleId(roleId);
		userRepository.save(user);
	}

	public boolean isItUserRole(HttpServletRequest request, String roleType) {
		long userId = CookieUtil.getUserId(request);
		return userRepository.isRoleMatchByUserIdRoleName(userId, roleType);
	}

	public void saveUser(User user) {
		long roleId = roleService.getRoleIdByType(RoleType.USER);
		user.setRoleId(roleId);
		userRepository.save(user.toUserEntity());
	}

	public boolean isUsernameExist(String username) {
		return userRepository.isUsernameExist(username);
	}

	public boolean isPasswordMatches(HttpServletRequest request, String password) {
		String hashedPassword = EncryptionUtil.hashData(password);
		String userPassword = userRepository.getPasswordByUserId(CookieUtil.getUserId(request));
		return hashedPassword.equals(userPassword);
	}

	public Long getUserIdByUser(User user) {
		return userRepository.getIdByUsername(user.getUsername());
	}

	public boolean isUserExistByUsernamePassword(User user) {
		return userRepository.isUserExistByUsernamePassword(user.getUsername(),
															EncryptionUtil.hashData(user.getPassword()));
	}

	public void fillInUser(User user) {
		UserEntity userEntity = userRepository.getUserByUsername(user.getUsername());
		user.copyFromUserEntity(userEntity);
	}

	public User getUserInfo(HttpServletRequest request) {
		long userId = CookieUtil.getUserId(request);

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
		long userId = CookieUtil.getUserId(request);
		UserEntity userEntity = userRepository.getUserEntityByUserId(userId);
		long roleId = userEntity.getRoleId();
		long groupId = userEntity.getGroupId();

		fillUser(user, userId, roleId, groupId);
		if(!user.getPassword().isEmpty()) {
			userRepository.save(user.toUserEntityWithPasswordEncryption());
			return;
		}
		user.setPassword(userEntity.getPassword());
		userRepository.save(user.toUserEntityWithOutPasswordEncryption());
	}

	public void deleteUserByUserId(HttpServletRequest request) {
		long userId = CookieUtil.getUserId(request);
		userRepository.deleteById(userId);
	}

	public String getUserRoleByUserId(HttpServletRequest request) {
		long userId = CookieUtil.getUserId(request);
		return userRepository.getUserRoleByUserId(userId);
	}

	private void fillUser(User user, long userId, long roleId, long groupId) {
		user.setUserId(userId);
		user.setRoleId(roleId);
		user.setGroupId(groupId);
	}
}
