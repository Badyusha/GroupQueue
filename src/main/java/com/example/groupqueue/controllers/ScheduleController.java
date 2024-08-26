package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.GroupSchedule;
import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.services.ScheduleService;
import com.example.groupqueue.utils.CookieUtil;
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
	private final ScheduleService scheduleService;

	@GetMapping("/schedule/get")
	public Schedule getSchedule(HttpServletRequest request) {
		return scheduleService.getDayOfWeekSchedule(request);
	}

	@GetMapping("/schedule/group/get")
	@ResponseBody
	public List<GroupSchedule> getGroupSchedules(HttpServletRequest request) {
		long groupId = CookieUtil.getGroupId(request);
		return scheduleService.getGroupSchedulesByGroupId(groupId);
	}

	@GetMapping("/week/get/current")
	public int getCurrentWeek() {
		return scheduleService.getCurrentWeek();
	}
}
