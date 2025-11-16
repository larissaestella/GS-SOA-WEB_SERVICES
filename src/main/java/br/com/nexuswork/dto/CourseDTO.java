package br.com.nexuswork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public record CourseDTO(
        Long id,
        @NotBlank String title,
        String description,
        @Min(1) int difficulty,
        Integer points
) {}
