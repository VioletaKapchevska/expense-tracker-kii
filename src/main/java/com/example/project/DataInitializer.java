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
    public void init()
    {
        roles = new ArrayList<>();
        //   if (this.roleRepository.count() == 0) {
        roles.add(new Role("ROLE_ADMIN"));
        roles.add(new Role("ROLE_USER"));
        this.roleRepository.saveAll(roles);
        //    }

        users = new ArrayList<>();
        // if (this.userRepository.count() == 0) {
        // String username, String password, String name, String surname, String email, String phoneNumber
        users.add(new User("anabanana", passwordEncoder.encode("ana123"),  "Ana", "Gjurchinova", "agjurcinova@gmail.com","123123123"));
        users.add(new User("kikaz", passwordEncoder.encode("kika123"), "Hristina", "Zdraveska", "zdeaveskah@gmail.com","234234234"));
        this.userRepository.saveAll(users);
        //  }

        categories = new ArrayList<>();
        categories.add(new Category("Personal assets"));
        categories.add(new Category("Household"));
        categories.add(new Category("Savings & Investment"));
        categories.add(new Category("Travel & Vacation"));
        categories.add(new Category("Debt Payment"));
        categories.add(new Category("Other"));
        this.categoryRepository.saveAll(categories);
    }
}

