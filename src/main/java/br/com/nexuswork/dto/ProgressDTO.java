package br.com.nexuswork.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProgressDTO(
        Long id,
        @NotNull Long userId,
        @NotNull Long courseId,
        @Min(0) @Max(100) double progress,
        boolean completed
) {}
