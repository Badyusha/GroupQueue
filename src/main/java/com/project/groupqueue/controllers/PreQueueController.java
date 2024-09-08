package com.project.groupqueue.controllers;

import com.project.groupqueue.models.dto.PreQueue;
import com.project.groupqueue.services.PreQueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PreQueueController {
	private final PreQueueService preQueueService;

	@PostMapping("/pre_queue/register/student")
	public void registerStudentToPreQueue(HttpServletRequest request, @RequestBody PreQueue preQueue) {
		preQueueService.addStudentToPreQueue(request, preQueue);
	}

	@DeleteMapping("/pre_queue/remove/student")
	public void removeStudentFromPreQueue(@RequestBody Long lessonId, HttpServletRequest request) {
		preQueueService.removeStudentFromPreQueueByLessonId(request, lessonId);
	}

	@PostMapping("/pre_queue/change_passing_labs")
	@ResponseBody
	public void changePassingLabs(HttpServletRequest request, @RequestBody PreQueue preQueue) {
		preQueueService.changePassingLabs(request, preQueue);
	}
}
