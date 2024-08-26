package com.example.groupqueue.comparators;

import com.example.groupqueue.models.dto.Pair;
import com.example.groupqueue.models.entities.PreQueueEntity;
import com.example.groupqueue.utils.GenerateQueueUtil;

import java.util.Comparator;

public class HighestPassingLabsSumComparator implements Comparator<PreQueueEntity> {
	@Override
	public int compare(PreQueueEntity o1, PreQueueEntity o2) {
		Pair<Integer, Integer> labsSum = GenerateQueueUtil.getPreQueueEntityLabSum(o1, o2);
		return Integer.compare(labsSum.getSecond(), labsSum.getFirst());
	}
}
