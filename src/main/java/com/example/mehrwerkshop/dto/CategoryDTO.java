package com.example.mehrwerkshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryDTO(
        @NonNull String id,
        String name
) {
}
