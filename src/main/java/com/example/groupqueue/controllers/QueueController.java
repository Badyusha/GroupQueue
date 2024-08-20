package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.QueueInfo;
import com.example.groupqueue.repo.QueueRepository;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QueueController {
	private final QueueRepository queueRepository;

	@GetMapping("/queue/get")
	@ResponseBody
	public List<QueueInfo> getQueues(HttpServletRequest request) {
		System.err.println("hello");
		long userId = CookieUtils.getUserId(request);
		int groupNumber = CookieUtils.getGroupNumber(request);
		List<Object[]> objects = queueRepository.getQueueInfoByUserIdGroupId(userId, groupNumber);

		System.err.println(objects);

		for(Object[] object : objects) {
			System.err.println((String) object[0]);
			System.err.println((String) object[1]);
			System.err.println(object[2]);
			System.err.println(object[3]);
			System.err.println((String) object[4]);
			System.err.println(object[5]);
			System.err.println(object[6]);
			System.err.println(object[7]);
			System.err.println(object[8]);
			System.err.println(object[9]);
			System.err.println("=========================================");
		}

		return null;
	}
}
