package com.example.groupqueue.comparators;

import com.example.groupqueue.models.entities.PreQueueEntity;
import com.example.groupqueue.utils.EncryptionUtil;

import java.util.Comparator;

public class LowestPassingLabsLengthComparator implements Comparator<PreQueueEntity> {
	@Override
	public int compare(PreQueueEntity o1, PreQueueEntity o2) {
		int o1Len = EncryptionUtil.convertByteArrayToIntArray(o1.getPassingLabs()).length;
		int o2Len = EncryptionUtil.convertByteArrayToIntArray(o2.getPassingLabs()).length;
		return Integer.compare(o1Len, o2Len);
	}
}
