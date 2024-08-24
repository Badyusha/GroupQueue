package com.example.groupqueue.models.enums;

import com.example.groupqueue.exceptions.SortTypeException;

public enum SortType {
	SIMPLE,
	RANDOM,
	HIGHEST_LAB,
	HIGHEST_LAB_SUM;

	public static SortType toSortType(String sortType) {
		return switch(sortType) {
			case "SIMPLE" -> SIMPLE;
			case "RANDOM" -> RANDOM;
			case "HIGHEST_LAB" -> HIGHEST_LAB;
			case "HIGHEST_LAB_SUM" -> HIGHEST_LAB_SUM;
			default -> throw new SortTypeException("unexpected value: " + sortType);
		};
	}
}
