package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.Schedule;
import com.example.groupqueue.services.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ScheduleController {
	private final ScheduleService scheduleService;

	// TODO
	@GetMapping("/schedule/get")
	public Schedule getSchedule(HttpServletRequest request) {
		return scheduleService.getDayOfWeekScheduleList(request);
	}
}
