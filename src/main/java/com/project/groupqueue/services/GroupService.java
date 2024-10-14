package com.project.groupqueue.services;

import com.project.groupqueue.external.api.BsuirAPI;
import com.project.groupqueue.repo.GroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
	private final GroupRepository groupRepository;

	public boolean groupExists(int groupNumber) {
		if(isGroupExist(groupNumber)) {
			return true;
		}
		return BsuirAPI.isGroupExist(groupNumber);
	}

	public boolean isGroupExist(int groupNumber) {
		return groupRepository.isGroupExist(groupNumber);
	}
}
