package ru.phyllosedis.judgeapi.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class ApiKeyGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    public String generate() {
        byte[] randomBytes = new byte[32]; // 256 бит энтропии
        SECURE_RANDOM.nextBytes(randomBytes);
        return "jdg_" + ENCODER.encodeToString(randomBytes);
    }
}
