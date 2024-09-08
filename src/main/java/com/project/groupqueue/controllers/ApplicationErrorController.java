package com.project.groupqueue.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class ApplicationErrorController implements ErrorController {
	@RequestMapping("/error")
	public String errorMapping() {
		return "/views/errorPage/error";
	}
}
