package com.example.groupqueue.exceptions;

public class WeekTypeException extends RuntimeException {
	public WeekTypeException(String errorMessage) {
		super("WeekType exception: " + errorMessage);
	}
}
