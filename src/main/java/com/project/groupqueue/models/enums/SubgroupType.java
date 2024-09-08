package com.project.groupqueue.models.enums;

public enum SubgroupType {
	FIRST,
	SECOND,
	ALL;

	public static SubgroupType getSubgroupType(int subgroup) {
		return switch(subgroup) {
			case 0 -> ALL;
			case 1 -> FIRST;
			case 2 -> SECOND;
			default -> null;
		};
	}
}
