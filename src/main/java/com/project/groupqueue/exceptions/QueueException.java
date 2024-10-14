package com.project.groupqueue.exceptions;

public class QueueException extends RuntimeException {
	public QueueException(String error) {
		super("Queue exception: " + error);
	}
}
