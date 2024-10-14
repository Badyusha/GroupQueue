package com.project.groupqueue.exceptions;

public class WeekTypeException extends RuntimeException {
	public WeekTypeException(String errorMessage) {
		super("WeekType exception: " + errorMessage);
	}
}
