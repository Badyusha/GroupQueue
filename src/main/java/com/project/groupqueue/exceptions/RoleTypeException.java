package com.project.groupqueue.exceptions;

public class RoleTypeException extends RuntimeException {
	public RoleTypeException(String errorMessage) {
		super("RoleType exception: " + errorMessage);
	}
}
