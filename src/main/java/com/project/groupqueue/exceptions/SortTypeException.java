package com.project.groupqueue.exceptions;

public class SortTypeException extends RuntimeException {
	public SortTypeException(String error) {
		super("Sort type exception: " + error);
	}
}
