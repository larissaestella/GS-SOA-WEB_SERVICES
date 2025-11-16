package br.com.nexuswork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AIQuestionDTO(
        @NotNull Long userId,
        @NotNull Long courseId,
        @NotBlank String question,
        String courseContext
) {}
