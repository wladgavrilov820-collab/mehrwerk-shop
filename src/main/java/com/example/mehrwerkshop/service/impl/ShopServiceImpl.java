package com.example.mehrwerkshop.service.impl;

import com.example.mehrwerkshop.dto.CategoryDTO;
import com.example.mehrwerkshop.dto.ShopDTO;
import com.example.mehrwerkshop.entity.CategoryEntity;
import com.example.mehrwerkshop.entity.ShopEntity;
import com.example.mehrwerkshop.repository.CategoryRepository;
import com.example.mehrwerkshop.repository.ShopRepository;
import com.example.mehrwerkshop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {


    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional(readOnly = true)
    @Cacheable("shops")
    public Page<ShopDTO> getShops(Pageable pageable) {
        return shopRepository.findAll(pageable)
                .map(shop -> new ShopDTO(
                        shop.getId(),
                        shop.getName(),
                        shop.getDescription(),
                        shop.getCategories().stream()
                                .map(category -> new CategoryDTO(
                                        category.getId(),
                                        category.getName()
                                ))
                                .toList()
                ));
    }

    @Override
    @Transactional
    @CacheEvict(value = "shops", allEntries = true)
    public void saveAllShops(List<ShopDTO> shopDTOS) {
        Map<String, ShopEntity> existingShops =
                shopRepository.findAll().stream()
                        .collect(Collectors.toMap(
                                ShopEntity::getId,
                                Function.identity()
                        ));

        Map<String, CategoryEntity> existingCategories =
                categoryRepository.findAll().stream()
                        .collect(Collectors.toMap(
                                CategoryEntity::getId,
                                Function.identity()
                        ));

        List<ShopEntity> entities = new ArrayList<>();

        for (ShopDTO dto : shopDTOS) {

            ShopEntity shop = existingShops.computeIfAbsent(
                    dto.id(),
                    id -> {
                        ShopEntity entity = new ShopEntity();
                        entity.setId(id);
                        return entity;
                    }
            );

            shop.setName(dto.name());
            shop.setDescription(dto.description());

            Set<CategoryEntity> categories = new HashSet<>();

            if (dto.categories() != null) {
                for (CategoryDTO categoryDto : dto.categories()) {

                    CategoryEntity category =
                            existingCategories.computeIfAbsent(
                                    categoryDto.id(),
                                    id -> {
                                        CategoryEntity entity = new CategoryEntity();
                                        entity.setId(id);
                                        return entity;
                                    }
                            );

                    category.setName(categoryDto.name());
                    categories.add(category);
                }
            }

            shop.setCategories(categories);
            entities.add(shop);
        }

        categoryRepository.saveAll(existingCategories.values());
        shopRepository.saveAll(entities);
    }
}
