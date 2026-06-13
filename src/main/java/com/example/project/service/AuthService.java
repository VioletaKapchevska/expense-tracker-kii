package com.example.project.service;

import org.springframework.stereotype.Service;
import com.example.project.model.User;

public interface AuthService {
    User login(String username, String password);
}
