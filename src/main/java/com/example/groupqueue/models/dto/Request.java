package com.example.groupqueue.models.dto;

import com.example.groupqueue.models.enums.RequestType;
import com.example.groupqueue.models.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
	private long studentId;
	private String lastName;
	private String firstName;
	private String username;
	private RoleType roleType;
	private int groupNumber;
	private RequestType requestType;
}
