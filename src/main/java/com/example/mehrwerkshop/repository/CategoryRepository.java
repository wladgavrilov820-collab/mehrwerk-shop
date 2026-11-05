package com.example.mehrwerkshop.repository;

import com.example.mehrwerkshop.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
}