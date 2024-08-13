package com.example.groupqueue.exceptions;

public class ScheduleException extends RuntimeException {
	public ScheduleException(String errorMessage) {
		super("Schedule exception: " + errorMessage);
	}
}
