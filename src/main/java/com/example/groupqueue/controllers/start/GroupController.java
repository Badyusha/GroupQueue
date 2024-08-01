package com.example.groupqueue.controllers.start;

import com.example.groupqueue.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class GroupController {
	private final GroupService groupService;

	@Autowired
	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping("/group_exists/{groupNumber}")
	public Integer groupExists(@PathVariable Integer groupNumber) {
		boolean isGroupInDb = groupService.isGroupInDb(groupNumber);

		return (isGroupInDb) ? HttpStatus.OK.value() : groupService.makeGroupExistsRequest(groupNumber).value();
	}
}
