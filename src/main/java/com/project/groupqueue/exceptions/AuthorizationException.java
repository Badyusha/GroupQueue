package com.project.groupqueue.exceptions;

public class AuthorizationException extends RuntimeException {
	public AuthorizationException(String errorMessage) {
		super("Failed to authorize: " + errorMessage);
	}
}
