package ru.phyllosedis.judgedomain.dto.company;


import java.util.UUID;

public record Company(
        UUID id,
        String name,
        String passwordHash,
        String apiKey
) {
}
