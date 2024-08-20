package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.PreQueue;
import com.example.groupqueue.services.PreQueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PreQueueController {
	private final PreQueueService preQueueService;

	@PostMapping("/pre_queue/register/user")
	public void registerUserToPreQueue(HttpServletRequest request, @RequestBody PreQueue preQueue) {
		preQueueService.addUserToPreQueue(request, preQueue);
	}

	@PostMapping("/pre_queue/remove/user")
	public void removeUserFromPreQueue(@RequestBody Long lessonId) {
		preQueueService.removeUserFromPreQueueByLessonId(lessonId);
	}
}
