package com.example.groupqueue.services;

import com.example.groupqueue.models.enums.DayOfWeek;
import com.example.groupqueue.repo.GroupRepository;
import com.example.groupqueue.repo.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class GroupService {
	private final GroupRepository groupRepository;
	private static final String groupScheduleUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=";

	@Autowired
	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}


	public HttpStatusCode makeGroupExistsRequest(Integer groupNumber) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			restTemplate.getForEntity(groupScheduleUrl + groupNumber.toString(), String.class);
		} catch(HttpClientErrorException | HttpServerErrorException e) {
			return HttpStatus.NOT_FOUND;
		}

		groupRepository.saveGroup(groupNumber);
		return HttpStatus.OK;
	}

	public boolean isGroupInDb(Integer groupNumber) {
		return groupRepository.isGroupInDb(groupNumber);
	}


	public Long getGroupIdByUserId(Long userId) {
		return groupRepository.getGroupIdByUserId(userId);
	}

	public Integer getGroupNumberByUserId(Long userId) {
		return groupRepository.getGroupNumberByUserId(userId);
	}

	public Integer getGroupNumberById(Long groupId) {
		return groupRepository.getGroupNumberById(groupId);
	}

	public Long getGroupIdByNumber(Integer groupNumber) {
		return groupRepository.getGroupIdByNumber(groupNumber);
	}
}
