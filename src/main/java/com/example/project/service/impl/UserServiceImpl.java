package com.example.project.service.impl;

import com.example.project.exceptions.*;
import com.example.project.model.Role;
import com.example.project.model.VerificationToken;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.repository.VerificationTokenRepository;
import com.example.project.service.EmailService;
import com.example.project.service.UserService;
import com.example.project.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    // TODO: 07.6.2025 password length, phone format...
    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, String email, String phoneNumber) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsMismatchException("Passwords do not match.");
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        if(this.userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists..");
        }

        if(!validate(email)){
            throw new InvalidEmailFormatException();
        }

        if(this.userRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw new PhoneNumberAlreadyInUseException("Phone number already exists..");
        }

        // TODO: 04.6.2025 fix adding correct roles accordingly
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found in database."));

        User user = new User(
                username,
                passwordEncoder.encode(password),
                name,
                surname,
                email,
                phoneNumber,
                List.of(userRole)
        );
        user.setEnabled(true);
        userRepository.save(user);
        // TODO: 04.6.2025 CHECK PHONE VALIDATION

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        VerificationToken vt = new VerificationToken(token, user, expiryDate);
        verificationTokenRepository.save(vt);

        String link = "http://localhost:8080/verification/token?token=" + token;
        //emailService.sendEmail(email, link);

        return user;
    }

    public String verifyToken(String token) {
        Optional<VerificationToken> optional = verificationTokenRepository.findByToken(token);
        if (optional.isEmpty() || optional.get().isExpired()) {
            return "Invalid or expired token";
        }

        User user = optional.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(optional.get());
        return "Email verified!";
    }

    @Override
    public User changePassword(String username, String oldPassword, String newPassword, String repeatPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordsMismatchException("Your old password is incorrect.");
        }
        if (!newPassword.equals(repeatPassword)) {
            throw new PasswordsMismatchException("Passwords do not match.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }

        return user;
    }
}

