package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.GroupQueue;
import com.example.groupqueue.models.dto.QueueInfo;
import com.example.groupqueue.repo.QueueRepository;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueRepository queueRepository;

	public List<QueueInfo> getQueueInfoByUserIdGroupId(HttpServletRequest request) {
		long userId = CookieUtils.getUserId(request);
		List<QueueInfo> queueResults = queueRepository.findQueueResults(userId);
		List<QueueInfo> preQueueResults = queueRepository.findPreQueueResults(userId);

		List<QueueInfo> combinedResults = new ArrayList<>();
		combinedResults.addAll(queueResults);
		combinedResults.addAll(preQueueResults);

		return combinedResults;
	}

	public List<GroupQueue> getGroupQueueByLessonId(long lessonId) {
		return queueRepository.getGroupQueueByLessonId(lessonId);
	}
}
