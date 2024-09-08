package com.project.groupqueue.services;

import com.project.groupqueue.models.dto.Request;
import com.project.groupqueue.models.entities.RequestEntity;
import com.project.groupqueue.models.enums.RequestType;
import com.project.groupqueue.repo.RequestRepository;
import com.project.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
	private final RequestRepository requestRepository;
	private final StudentService studentService;

	public List<Request> getRequests() {
		return requestRepository.getRequests();
	}

	public void sendBecomeGroupAdmin(HttpServletRequest request) {
		long studentId = CookieUtil.getStudentId(request);
		requestRepository.save(new RequestEntity(RequestType.BECOME_GROUP_ADMIN, studentId));
	}

	public void acceptBecomeGroupAdminRequest(Request requestDto) {
		long studentId = requestDto.getStudentId();
		studentService.updateStudentRoleType(studentId);

		long requestId = requestRepository.getRequestIdByRequestTypeStudentId(requestDto.getRequestType(), studentId);
		requestRepository.deleteById(requestId);
	}

	public void declineBecomeGroupAdminRequest(Request requestDto) {
		long requestId = requestRepository.getRequestIdByRequestTypeStudentId(requestDto.getRequestType(),
																				requestDto.getStudentId());
		requestRepository.deleteById(requestId);
	}
}
