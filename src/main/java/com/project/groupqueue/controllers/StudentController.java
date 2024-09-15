package com.project.groupqueue.controllers;

import com.project.groupqueue.models.dto.Student;
import com.project.groupqueue.services.StudentService;
import com.project.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;

	@GetMapping("/student/main_page")
	public String mainPage(HttpServletRequest request) {
		if(!CookieUtil.isCookiesExists(request)) {
			return "redirect:/";
		}
		return "views/student/mainPage";
	}

	@GetMapping("/student/queues")
	public String getQueues(HttpServletRequest request) {
		if(!CookieUtil.isCookiesExists(request)) {
			return "redirect:/";
		}
		return "views/student/studentQueues";
	}

	@GetMapping("/student/username/{username}/exists")
	@ResponseBody
	public boolean isUsernameExist(@PathVariable String username) {
		return studentService.isUsernameExist(username);
	}

	@GetMapping("/student/{roleType}/is_student_role")
	@ResponseBody
	public boolean isItStudentRoleType(HttpServletRequest request, @PathVariable String roleType) {
		return studentService.isItStudentRole(request, roleType);
	}

	@GetMapping("/student/password/{password}/matches")
	@ResponseBody
	public boolean isPasswordMatches(HttpServletRequest request, @PathVariable String password) {
		return studentService.isPasswordMatches(request, password);
	}

	@GetMapping("/student/get/info")
	@ResponseBody
	public Student getStudentInfo(HttpServletRequest request) {
		return studentService.getStudentInfo(request);
	}

	@PostMapping("/student/edit_profile")
	public void editProfile(HttpServletResponse response, HttpServletRequest request, @RequestBody Student student) {
		studentService.editProfile(request, student);
		CookieUtil.addRequired(response, student);
	}

	@DeleteMapping("/student/delete")
	public void deleteStudent(HttpServletRequest request, HttpServletResponse response) {
		studentService.deleteStudentByStudentId(request);
		CookieUtil.deleteAllCookies(response, request);
	}

	@GetMapping("/student/get/role")
	@ResponseBody
	public String getStudentRole(HttpServletRequest request) {
		return studentService.getStudentRoleByStudentId(request);
	}
}
