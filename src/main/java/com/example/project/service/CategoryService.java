package com.example.project.service;

import com.example.project.model.Category;

import java.util.List;

public interface CategoryService {
    Category find(Long id);
    List<Category> findAll();
}
