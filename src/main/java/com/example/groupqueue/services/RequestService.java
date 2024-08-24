package com.example.groupqueue.services;

import com.example.groupqueue.models.dto.Request;
import com.example.groupqueue.models.entities.RequestEntity;
import com.example.groupqueue.models.enums.RequestType;
import com.example.groupqueue.repo.RequestRepository;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
	private final RequestRepository requestRepository;
	private final UserService userService;

	public List<Request> getRequests() {
		return requestRepository.getRequests();
	}

	public void sendBecomeGroupAdmin(HttpServletRequest request) {
		long userId = CookieUtils.getUserId(request);
		requestRepository.save(new RequestEntity(RequestType.BECOME_GROUP_ADMIN, userId));
	}

	public void acceptBecomeGroupAdminRequest(Request requestDto) {
		long userId = requestDto.getUserId();
		userService.updateUserRoleType(userId);

		long requestId = requestRepository.getRequestIdByRequestTypeUserId(requestDto.getRequestType(), userId);
		requestRepository.deleteById(requestId);
	}

	public void declineBecomeGroupAdminRequest(Request requestDto) {
		long requestId = requestRepository.getRequestIdByRequestTypeUserId(requestDto.getRequestType(),
				requestDto.getUserId());
		requestRepository.deleteById(requestId);
	}
}
