package com.example.groupqueue.exceptions;

public class CookieException extends RuntimeException {
	public CookieException(String errorMessage) {
		super("Cookie exception: " + errorMessage);
	}
}
