package com.example.project.web;

import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    // TODO: 16.6.2025 PROFILE TEMPLATE TO SHOW USER INFO 
    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal User user,
                                 Model model){
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/change/password")
    public String getChangePasswordForm() {
        return "change-password";
    }

    // TODO: 16.6.2025 TEMPLATE WITH A FORM TO CHANGE PASSWORD 
    @PostMapping("/change/password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String repeatPassword,
                                 @AuthenticationPrincipal User user,
                                 Model model){
        try{
            userService.changePassword(user.getUsername(), oldPassword, newPassword, repeatPassword);
            model.addAttribute("message", "Your password has been changed successfully.");
            return "redirect:/profile";
        }catch (RuntimeException ex){
            model.addAttribute("hasError", true);
            model.addAttribute("message", ex.getMessage());
            return "change-password"; // return the password changing form template again.
        }
    }
}
