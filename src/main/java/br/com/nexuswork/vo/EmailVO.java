package br.com.nexuswork.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVO(
        @NotBlank
        @Email
        String value) {}
