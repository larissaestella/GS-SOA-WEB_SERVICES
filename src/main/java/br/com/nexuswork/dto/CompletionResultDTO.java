package br.com.nexuswork.dto;

public record CompletionResultDTO(
        Long userId,
        Long courseId,
        int pointsGained,
        int totalPoints,
        int oldLevel,
        int newLevel,
        boolean leveledUp,
        String message
) {}
