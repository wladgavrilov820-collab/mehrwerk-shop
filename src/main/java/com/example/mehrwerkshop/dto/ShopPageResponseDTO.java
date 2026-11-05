package com.example.mehrwerkshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShopPageResponseDTO(
        int currentPage,
        int numberOfPages,
        int numberOfResults,
        List<ShopDTO> items
) {
}
