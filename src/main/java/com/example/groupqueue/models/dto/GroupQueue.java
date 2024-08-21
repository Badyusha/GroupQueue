package com.example.groupqueue.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupQueue {
	private int numberInQueue;
	private String lastName;
	private String firstName;
	private String username;
	private byte[] passingLabs;
}
