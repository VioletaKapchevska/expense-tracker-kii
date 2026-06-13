package com.example.project.service.impl;

import com.example.project.exceptions.AccountNotVerifiedException;
import com.example.project.exceptions.InvalidArgumentsException;
import com.example.project.repository.UserRepository;
import com.example.project.service.AuthService;
import com.example.project.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidArgumentsException::new);

        if(!user.isEnabled()){
            throw new AccountNotVerifiedException();
        }
        return user;
    }

}

