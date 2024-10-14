package com.project.groupqueue.utils;

import com.project.groupqueue.comparators.*;
import com.project.groupqueue.models.dto.Pair;
import com.project.groupqueue.models.entities.PreQueueEntity;
import com.project.groupqueue.models.entities.QueueEntity;
import com.project.groupqueue.models.enums.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateQueueUtil {
	public static List<QueueEntity> simple(List<PreQueueEntity> preQueueEntityList) {
		preQueueEntityList.sort(new RegistrationTimeComparator());
		return createOrderedQueueEntityList(preQueueEntityList);
	}

	public static List<QueueEntity> random(List<PreQueueEntity> preQueueEntityList) {
		Collections.shuffle(preQueueEntityList);
		return createOrderedQueueEntityList(preQueueEntityList);
	}

	public static List<QueueEntity> highestLabCount(List<PreQueueEntity> preQueueEntityList) {
		Collections.shuffle(preQueueEntityList);
		preQueueEntityList.sort(new HighestPassingLabsLengthComparator());

		return createOrderedQueueEntityList(preQueueEntityList);
	}

	public static List<QueueEntity> lowestLabCount(List<PreQueueEntity> preQueueEntityList) {
		Collections.shuffle(preQueueEntityList);
		preQueueEntityList.sort(new LowestPassingLabsLengthComparator());

		return createOrderedQueueEntityList(preQueueEntityList);
	}

	public static List<QueueEntity> highestLabSum(List<PreQueueEntity> preQueueEntityList) {
		Collections.shuffle(preQueueEntityList);
		preQueueEntityList.sort(new HighestPassingLabsSumComparator());

		return createOrderedQueueEntityList(preQueueEntityList);
	}

	public static List<QueueEntity> lowestLabSum(List<PreQueueEntity> preQueueEntityList) {
		Collections.shuffle(preQueueEntityList);
		preQueueEntityList.sort(new LowestPassingLabsSumComparator());

		return createOrderedQueueEntityList(preQueueEntityList);
	}



	public static Pair<Integer, Integer> getPreQueueEntityLabSum(PreQueueEntity o1, PreQueueEntity o2) {
		int[] o1Labs = EncryptionUtil.convertByteArrayToIntArray(o1.getPassingLabs()),
			  o2Labs = EncryptionUtil.convertByteArrayToIntArray(o2.getPassingLabs());
		int o1Sum = 0,
			o2Sum = 0;

		for(int labNumber : o1Labs) {
			o1Sum += labNumber;
		}

		for(int labNumber : o2Labs) {
			o2Sum += labNumber;
		}
		return new Pair<>(o1Sum, o2Sum);
	}

	public static boolean isRegistrationOpen(DayOfWeek dayOfWeek, LocalTime startTime) {
		LocalTime now = LocalTime.now();
		java.time.DayOfWeek todayDayOfWeek = LocalDate.now().getDayOfWeek();
		java.time.DayOfWeek dayOfWeek_JavaTime = DayOfWeek.getJavaTimeDayOfWeek(dayOfWeek);
		boolean isTodayDayBeforeLab = todayDayOfWeek.plus(1).equals(dayOfWeek_JavaTime);
		boolean isNowTimeForRegistration = !now.isBefore(DayOfWeek.TIME_FOR_REGISTRATION);

		boolean isTodayLabDay = todayDayOfWeek.equals(dayOfWeek_JavaTime);
		boolean isNowTimeBeforeLabStart = !now.isAfter(startTime.minusHours(1));

		return (isTodayDayBeforeLab && isNowTimeForRegistration) || (isTodayLabDay && isNowTimeBeforeLabStart);
	}



	private static List<QueueEntity> createOrderedQueueEntityList(List<PreQueueEntity> preQueueEntityList) {
		int preQueueSize = preQueueEntityList.size();
		List<QueueEntity> queueEntityList = new ArrayList<>(preQueueSize);
		for(int index = 0; index < preQueueSize; ++index) {
			PreQueueEntity preQueueEntity = preQueueEntityList.get(index);
			queueEntityList.add(preQueueEntity.toQueueEntity(index + 1));
		}
		return queueEntityList;
	}

}
