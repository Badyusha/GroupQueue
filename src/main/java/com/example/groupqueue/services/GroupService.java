package com.example.groupqueue.services;

import com.example.groupqueue.external.api.BsuirAPI;
import com.example.groupqueue.repo.GroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
	private final GroupRepository groupRepository;
	private final ScheduleService scheduleService;

	public boolean checkGroupExistenceViaApi(int groupNumber) {
		if(!BsuirAPI.isGroupExist(groupNumber)) {
			return false;
		}
		groupRepository.saveGroup(groupNumber);
		scheduleService.addRecordsForNewGroupByGroupNumber(groupNumber);
		return true;
	}

	public boolean isGroupExist(int groupNumber) {
		return groupRepository.isGroupExist(groupNumber);
	}
	
	public Long getGroupIdByNumber(int groupNumber) {
		return groupRepository.getGroupIdByNumber(groupNumber);
	}
}
