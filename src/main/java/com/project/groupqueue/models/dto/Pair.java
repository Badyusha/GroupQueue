package com.project.groupqueue.models.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Pair<T, U> {
	private final T first;
	private final U second;

	public Pair(T first, U second) {
		this.first = first;
		this.second = second;
	}
}
