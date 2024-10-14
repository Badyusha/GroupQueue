package com.project.groupqueue.services;

import com.project.groupqueue.models.dto.GroupQueue;
import com.project.groupqueue.models.dto.QueueInfo;
import com.project.groupqueue.models.entities.LessonEntity;
import com.project.groupqueue.models.entities.PreQueueEntity;
import com.project.groupqueue.models.entities.QueueEntity;
import com.project.groupqueue.models.enums.SortType;
import com.project.groupqueue.repo.LessonRepository;
import com.project.groupqueue.repo.PreQueueRepository;
import com.project.groupqueue.repo.QueueRepository;
import com.project.groupqueue.utils.CookieUtil;
import com.project.groupqueue.utils.GenerateQueueUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueRepository queueRepository;
	private final LessonRepository lessonRepository;
	private final PreQueueRepository preQueueRepository;

	public List<QueueInfo> getQueueInfoByStudentIdGroupId(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		List<QueueInfo> queueResults = queueRepository.findQueueResultsByStudentId(studentId);
		List<QueueInfo> preQueueResults = queueRepository.findPreQueueResults(studentId);

		List<QueueInfo> combinedResults = new ArrayList<>();
		combinedResults.addAll(queueResults);
		combinedResults.addAll(preQueueResults);

		return combinedResults;
	}

	public List<GroupQueue> getGroupQueueByLessonId(long lessonId) {
		return queueRepository.getGroupQueueByLessonId(lessonId);
	}

	/**
	 * This method generate queues
	 * and run at the time in each @Scheduled annotation
	 */
	@Schedules({
			@Scheduled(cron = "0 0 8 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 35 9 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 25 11 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 0 13 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 50 14 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 25 16 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 0 18 * * *", zone = "Europe/Minsk"),
			@Scheduled(cron = "0 40 19 * * *", zone = "Europe/Minsk")
	})
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
