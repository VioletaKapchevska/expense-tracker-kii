package com.example.project.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.project.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String name, String surname,
                  String email, String phoneNumber);
    String verifyToken(String token);
    User changePassword(String username, String oldPassword, String newPassword, String repeatPassword);
}
