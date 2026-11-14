package ru.phyllosedis.judgedomain.dto.company;

import java.util.UUID;

public record AuthResponse(
        UUID companyId,
        String name,
        String apiKey
) {
}
