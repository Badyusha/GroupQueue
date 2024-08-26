package com.example.groupqueue.controllers;

import com.example.groupqueue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String start(HttpServletResponse response, HttpServletRequest request) {
        CookieUtil.deleteAllCookies(response, request);
        return "/views/startPage/startPageDesktop";
    }
}
