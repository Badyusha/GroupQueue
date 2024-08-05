package com.example.groupqueue.controllers;

import com.example.groupqueue.services.CookieService;
import com.example.groupqueue.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class StartController {
    //  METHODS
    @GetMapping("/")
    public String start(HttpServletResponse response, HttpServletRequest request) {
        CookieService.deleteAllCookies(response, request);
        return "/views/startPage/startPageDesktop";
    }
}
