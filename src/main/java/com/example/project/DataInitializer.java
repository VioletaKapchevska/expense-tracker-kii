package com.example.project;

import com.example.project.model.Category;
import com.example.project.model.Role;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    public static List<Role> roles = null;
    public static List<User> users = null;
    public static List<Category> categories = null;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
                           CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }

        if (userRepository.findByUsername("anabanana").isEmpty()) {
            userRepository.save(new User(
                    "anabanana",
                    passwordEncoder.encode("ana123"),
                    "Ana",
                    "Gjurchinova",
                    "agjurcinova@gmail.com",
                    "123123123"
            ));
        }

        if (userRepository.findByUsername("kikaz").isEmpty()) {
            userRepository.save(new User(
                    "kikaz",
                    passwordEncoder.encode("kika123"),
                    "Hristina",
                    "Zdraveska",
                    "zdeaveskah@gmail.com",
                    "234234234"
            ));
        }

        if (categoryRepository.count() == 0) {
            categories = new ArrayList<>();
            categories.add(new Category("Personal assets"));
            categories.add(new Category("Household"));
            categories.add(new Category("Savings & Investment"));
            categories.add(new Category("Travel & Vacation"));
            categories.add(new Category("Debt Payment"));
            categories.add(new Category("Other"));
            categoryRepository.saveAll(categories);
        }
    }
}

