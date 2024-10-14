package com.project.groupqueue.comparators;

import com.project.groupqueue.models.entities.PreQueueEntity;
import com.project.groupqueue.utils.EncryptionUtil;

import java.util.Comparator;

public class HighestPassingLabsLengthComparator implements Comparator<PreQueueEntity> {
	@Override
	public int compare(PreQueueEntity o1, PreQueueEntity o2) {
		int o1Len = EncryptionUtil.convertByteArrayToIntArray(o1.getPassingLabs()).length;
		int o2Len = EncryptionUtil.convertByteArrayToIntArray(o2.getPassingLabs()).length;
		return Integer.compare(o2Len, o1Len);
	}
}
