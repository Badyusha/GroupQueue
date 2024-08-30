package com.example.groupqueue.controllers;

import com.example.groupqueue.exceptions.PermissionException;
import com.example.groupqueue.models.dto.Request;
import com.example.groupqueue.models.enums.PermissionType;
import com.example.groupqueue.repo.PermissionRepository;
import com.example.groupqueue.services.RequestService;
import com.example.groupqueue.services.StudentService;
import com.example.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RequestController {
	private final RequestService requestService;
	private final StudentService studentService;
	private final PermissionRepository permissionRepository;

	@GetMapping("/request/become_group_admin")
	public String showBecomeGroupAdminRequests(HttpServletRequest request) {
		if(!CookieUtil.isCookiesExists(request)) {
			return "redirect:/";
		}
		
		long roleId = studentService.getRoleIdByStudentId(CookieUtil.getStudentId(request));
		if(permissionRepository.isActionAllowed(PermissionType.SHOW_BECOME_GROUP_ADMIN_REQUESTS, roleId)) {
			return "views/errorPage/permissionIsNotAllowed";
		}
		return "views/groupAdminRequests/groupAdminRequests";
	}

	@PostMapping("/request/send/become_group_admin")
	@ResponseBody
	public void sendBecomeGroupAdminRequest(HttpServletRequest request) {
		long roleId = studentService.getRoleIdByStudentId(CookieUtil.getStudentId(request));
		if(permissionRepository.isActionAllowed(PermissionType.BECOME_GROUP_ADMIN, roleId)) {
			throw new PermissionException("cannot become group admin");
		}
		requestService.sendBecomeGroupAdmin(request);
	}

	@GetMapping("/request/become_group_admin/get")
	@ResponseBody
	public List<Request> getBecomeGroupAdminRequests() {
		return requestService.getRequests();
	}

	@PostMapping("/request/become_group_admin/accept")
	@ResponseBody
	public void acceptBecomeGroupAdminRequest(@RequestBody Request requestDto) {
		System.err.println(requestDto);
		requestService.acceptBecomeGroupAdminRequest(requestDto);
	}

	@PostMapping("/request/become_group_admin/decline")
	@ResponseBody
	public void declineBecomeGroupAdminRequest(@RequestBody Request requestDto) {
		requestService.declineBecomeGroupAdminRequest(requestDto);
	}
}
