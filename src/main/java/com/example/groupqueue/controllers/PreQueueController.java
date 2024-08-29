package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.PreQueue;
import com.example.groupqueue.services.PreQueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
