package com.example.groupqueue.controllers;

import com.example.groupqueue.external.api.BsuirAPI;
import com.example.groupqueue.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class GroupController {
	private final GroupService groupService;

	@GetMapping("/group/number/{groupNumber}/exists")
	public boolean groupExists(@PathVariable Integer groupNumber) {
		return groupService.groupExists(groupNumber);
	}
}
