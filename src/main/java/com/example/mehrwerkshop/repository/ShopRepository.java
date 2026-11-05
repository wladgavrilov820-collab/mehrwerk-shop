package com.example.mehrwerkshop.repository;

import com.example.mehrwerkshop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<ShopEntity, String> {
}
