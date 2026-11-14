package ru.phyllosedis.judgedomain.dto.sandbox;

import ru.phyllosedis.judgedomain.Language;
import ru.phyllosedis.judgedomain.limit.ResourceLimits;

import java.util.UUID;

public record SandboxProfile(
        UUID id,
        UUID companyId,
        String name,
        Language language,
        ResourceLimits limits
) {}
