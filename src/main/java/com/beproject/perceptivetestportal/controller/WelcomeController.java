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

@GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "forward:/student-dashboard.html"; 
    }

    @GetMapping("/student/take-test")
    public String takeTestPage() {
        return "forward:/take-test.html"; 
    }

// --- STUDENT ROUTES ---

    @GetMapping("/student/quick-start-quiz")
    public String quickStartQuizPage() {
        return "forward:/quick-start-quiz.html"; 
    }

    @GetMapping("/student/exam-room")
    public String examRoomPage() {
        return "forward:/exam-room.html"; 
    }

    // Add this to your Student Routes in WelcomeController
    @GetMapping("/student/instructions")
    public String instructionsPage() {
        return "forward:/instructions.html"; 
    }

    @GetMapping("/student/test-summary")
    public String testSummaryPage() {
        return "forward:/test-summary.html"; 
    }
@GetMapping("/student/result")
    public String testResultPage() {
        return "forward:/result.html"; 
    }
}