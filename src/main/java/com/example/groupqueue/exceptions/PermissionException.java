package com.example.groupqueue.exceptions;

public class PermissionException extends RuntimeException {
	public PermissionException(String error) {
		super("Permission exception: " + error);
	}
}
