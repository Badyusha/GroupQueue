package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.repo.ScheduleRepository;
import com.example.groupqueue.services.CookieService;
import com.example.groupqueue.services.GroupService;
import com.example.groupqueue.services.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class ScheduleController {
	private final GroupService groupService;
	private final ScheduleService scheduleService;

	@Autowired
	public ScheduleController(GroupService groupService, ScheduleService scheduleService) {
		this.groupService = groupService;
		this.scheduleService = scheduleService;
	}

	@GetMapping("/get_schedule")
	public List<Schedule> getSchedule (HttpServletRequest request) {
		Integer groupNumber = groupService.getGroupNumberById(Long.parseLong(CookieService.getCookie(request, "groupId")));
		System.err.println(scheduleService.getGroupScheduleList(groupNumber));
		return null;
	}
}
