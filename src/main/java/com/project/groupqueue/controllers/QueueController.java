package com.project.groupqueue.controllers;

import com.project.groupqueue.models.dto.GroupQueue;
import com.project.groupqueue.models.dto.QueueInfo;
import com.project.groupqueue.services.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QueueController {
	private final QueueService queueService;

	@GetMapping("/queue/get")
	@ResponseBody
	public List<QueueInfo> getQueues(HttpServletRequest request) {
		return queueService.getQueueInfoByStudentIdGroupId(request);
	}

	@GetMapping("/queue/lesson/{lessonId}/get")
	@ResponseBody
	public List<GroupQueue> getGroupQueue(@PathVariable long lessonId) {
		return queueService.getGroupQueueByLessonId(lessonId);
	}
}
