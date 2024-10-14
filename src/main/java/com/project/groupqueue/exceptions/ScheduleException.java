package com.project.groupqueue.exceptions;

public class ScheduleException extends RuntimeException {
	public ScheduleException(String errorMessage) {
		super("Schedule exception: " + errorMessage);
	}
}
