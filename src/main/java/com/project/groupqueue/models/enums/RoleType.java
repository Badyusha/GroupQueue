package com.project.groupqueue.models.enums;

import com.project.groupqueue.exceptions.RoleTypeException;

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
