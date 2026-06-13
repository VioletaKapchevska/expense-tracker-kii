//package com.example.project.web;
//
//import com.example.project.service.AuthService;
//import com.example.project.model.User;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/login")
//public class LoginController {
//    private final AuthService authService;
//    public LoginController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    //@RequestMapping(method = RequestMethod.GET, value = "/login")
//    @GetMapping
//    public String getLoginPage() {
//        // Return the name of the Thymeleaf template that will be used to render the login page
//        return "login";
//    }
//    @PostMapping
//    public String login(HttpServletRequest request, Model model) {
//        User user = null;
//
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        try {
//            user = authService.login(username, password);
////            request.getSession().setAttribute("user", user);
//            // Redirect to the home page
//            return "redirect:/";
//        } catch (RuntimeException ex) {
//            model.addAttribute("hasError", true);
//            model.addAttribute("error", ex.getMessage());
//            return "login";
//        }
//    }
//
//}
//

package com.example.project.web;

import com.example.project.exceptions.AccountNotVerifiedException;
import com.example.project.exceptions.InvalidArgumentsException;
import com.example.project.model.User;
import com.example.project.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

}


