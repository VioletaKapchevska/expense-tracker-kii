package com.example.project.web;
import com.example.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// TODO: 07.6.2025 POP UP IF EXPIRED, TO BE ABLE TO REGENERATE A NEW VERIFICATION TOKEN
@Controller
@RequestMapping("/verification")
public class VerificationController {
    private final UserService userService;

    public VerificationController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String getVerificationInfoPage(){
        return "verification-info";
    }

    @GetMapping("/token")
    public String verifyToken(@RequestParam("token") String token, Model model) {
        String result = userService.verifyToken(token);
        model.addAttribute("message", result);
        return "verification-result";
    }
}

