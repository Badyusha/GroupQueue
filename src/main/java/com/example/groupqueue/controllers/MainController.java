package com.example.groupqueue.controllers;

import com.example.groupqueue.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    //  METHODS
    @GetMapping("/")
    public String start(HttpServletResponse response, HttpServletRequest request) {
        CookieUtils.deleteAllCookies(response, request);
        return "/views/startPage/startPageDesktop";
    }
}
