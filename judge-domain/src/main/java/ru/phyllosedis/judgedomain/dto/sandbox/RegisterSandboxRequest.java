package ru.phyllosedis.judgedomain.dto.sandbox;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.phyllosedis.judgedomain.Language;
import ru.phyllosedis.judgedomain.limit.ResourceLimits;

public record RegisterSandboxRequest(
        @NotBlank String name,
        @NotNull Language language,
        @Valid ResourceLimits limits
) {}
