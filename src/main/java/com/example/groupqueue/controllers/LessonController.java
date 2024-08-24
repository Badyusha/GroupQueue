package com.example.groupqueue.controllers;

import com.example.groupqueue.models.dto.GroupSchedule;
import com.example.groupqueue.models.enums.PermissionType;
import com.example.groupqueue.models.enums.SortType;
import com.example.groupqueue.repo.PermissionRepository;
import com.example.groupqueue.services.LessonService;
import com.example.groupqueue.services.UserService;
import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LessonController {
	private final LessonService lessonService;
	private final UserService userService;
	private final PermissionRepository permissionRepository;

	@GetMapping("/lesson/choose/sort_type")
	public String chooseSortType(HttpServletRequest request) {
		if(!CookieUtils.isCookiesExists(request)) {
			return "redirect:/";
		}

		long roleId = userService.getRoleIdByUserId(CookieUtils.getUserId(request));
		if(!permissionRepository.isActionAllowed(PermissionType.CHOOSE_SORT_TYPE, roleId)) {
			return "views/errorPage/permissionIsNotAllowed";
		}
		return "views/chooseSortType/chooseSortType";
	}

	@GetMapping("/lesson/sort_type/get")
	@ResponseBody
	public SortType[] getSortTypes() {
		return SortType.values();
	}

	@PostMapping("/lesson/change/sort_type")
	@ResponseBody
	public void changeLessonSortType(@RequestBody GroupSchedule groupSchedule) {
		lessonService.changeSortType(groupSchedule);
	}
}
