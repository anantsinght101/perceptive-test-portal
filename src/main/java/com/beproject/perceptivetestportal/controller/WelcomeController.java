package com.beproject.perceptivetestportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String index() {
        // This looks for src/main/resources/static/index.html
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/signup")
    public String signup() {
        return "forward:/signup.html";
    }


    @GetMapping("/teacher/dashboard")
public String teacherDashboard() {
    return "forward:/teacher-dashboard.html"; // Returns the static HTML file
}

@GetMapping("/teacher/create-test")
public String createTestPage() {
    return "forward:/create-test.html"; // Returns the static HTML file
}
}