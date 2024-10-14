package com.project.groupqueue.exceptions;

public class DayOfWeekException extends RuntimeException {
	public DayOfWeekException(String errorMessage) {
		super("DayOfWeek exception: " + errorMessage);
	}
}
