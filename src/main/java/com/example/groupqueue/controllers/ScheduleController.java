package com.example.groupqueue.controllers;

import com.example.groupqueue.models.entities.ScheduleEntity;
import com.example.groupqueue.services.CookieService;
import com.example.groupqueue.services.GroupService;
import com.example.groupqueue.services.ScheduleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ScheduleController {
	private final GroupService groupService;
	private final ScheduleService scheduleService;

	@GetMapping("/get_schedule")
	public List<ScheduleEntity> getSchedule(HttpServletRequest request) {
		Long groupId = CookieService.getGroupIdFromCookie(request);
		if (groupId == null) {
			return null;
		}

		Integer groupNumber = groupService.getGroupNumberById(groupId);
		return scheduleService.getGroupScheduleList(groupNumber);
	}
}
