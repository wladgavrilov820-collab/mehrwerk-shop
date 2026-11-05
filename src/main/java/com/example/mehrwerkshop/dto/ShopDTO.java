package com.example.mehrwerkshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NonNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShopDTO(
        @NonNull String id,
        String name,
        String description,
        List<CategoryDTO> categories
) {
}
