package ru.phyllosedis.judgedomain.dto.sandbox;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
        @NotBlank String name,
        @NotBlank @Size(min = 6) String password
) {}

