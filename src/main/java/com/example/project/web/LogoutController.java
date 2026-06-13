package com.example.project.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout-message")
public class LogoutController {

    @GetMapping( )
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout-message";
    }

}
