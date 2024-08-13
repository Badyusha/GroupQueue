package com.example.groupqueue.services;

import com.example.groupqueue.repo.GroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GroupService {
	private final GroupRepository groupRepository;
	private final ScheduleService scheduleService;
	private static final String GROUP_SCHEDULE_URL = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";

	public boolean checkGroupExistenceViaApi(int groupNumber) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.getForEntity(GROUP_SCHEDULE_URL + groupNumber, String.class);
		} catch(HttpClientErrorException e) {
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
