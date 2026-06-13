package com.example.project.web;

import com.example.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(){
        return "signup";
    }

    @PostMapping
    public String signup(Model model,
                         @RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String repeatPassword,
                         @RequestParam String name,
                         @RequestParam String surname,
                         @RequestParam String email,
                         @RequestParam String phoneNumber){
        try {
            userService.register(username, password, repeatPassword, name, surname, email, phoneNumber);
            model.addAttribute("email", email);
           // return "redirect:/verification";
            return "redirect:/login";
        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            System.out.println(ex.getMessage());
            return "signup";
        }
    }
}

