package com.example.groupqueue.models.enums;

import com.example.groupqueue.exceptions.RoleTypeException;
import com.example.groupqueue.models.dto.User;

public enum RoleType {
	USER,
	GROUP_ADMIN,
	SUDO;

	public static RoleType getRoleTypeByName(String name) {
		return switch(name) {
			case "USER" -> USER;
			case "GROUP_ADMIN" -> GROUP_ADMIN;
			case "SUDO" -> SUDO;
			default -> throw new RoleTypeException("Unexpected value: " + name);
		};
	}
}
