package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.GroupQueue;
import com.example.groupqueue.models.dto.QueueInfo;
import com.example.groupqueue.models.entities.LessonEntity;
import com.example.groupqueue.models.entities.PreQueueEntity;
import com.example.groupqueue.models.entities.QueueEntity;
import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.repo.LessonRepository;
import com.example.groupqueue.repo.PreQueueRepository;
import com.example.groupqueue.repo.QueueRepository;
import com.example.groupqueue.utils.CookieUtil;
import com.example.groupqueue.utils.GenerateQueueUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueRepository queueRepository;
	private LessonRepository lessonRepository;
	private PreQueueRepository preQueueRepository;

	public List<QueueInfo> getQueueInfoByUserIdGroupId(HttpServletRequest request) {
		long userId = CookieUtil.getUserId(request);
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


	@Scheduled(fixedRate = 60000) // runs every minute to check for lessons starting in an hour
	@Transactional
	public void generateQueueForUpcomingLessons() {
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTimeOneHourLater = LocalTime.now().plusHours(1);

		List<LessonEntity> upcomingLessons = lessonRepository.findLessonsStartingAt(currentDate, currentTimeOneHourLater);

		for (LessonEntity lesson : upcomingLessons) {
			List<PreQueueEntity> preQueueEntities = preQueueRepository.getPreQueueEntityListByLessonId(lesson.getId());
			List<QueueEntity> queueEntities = generateQueueBasedOnSortType(preQueueEntities, lesson.getSortType());
			queueRepository.saveAll(queueEntities);
		}
	}

	private List<QueueEntity> generateQueueBasedOnSortType(List<PreQueueEntity> preQueueEntities, SortType sortType) {
		return switch (sortType) {
			case SIMPLE -> GenerateQueueUtil.simple(preQueueEntities);
			case RANDOM -> GenerateQueueUtil.random(preQueueEntities);
			case HIGHEST_LAB_COUNT -> GenerateQueueUtil.highestLabCount(preQueueEntities);
			case LOWEST_LAB_COUNT -> GenerateQueueUtil.lowestLabCount(preQueueEntities);
			case HIGHEST_LAB_SUM -> GenerateQueueUtil.highestLabSum(preQueueEntities);
			case LOWEST_LAB_SUM -> GenerateQueueUtil.lowestLabSum(preQueueEntities);
		};
	}
}
