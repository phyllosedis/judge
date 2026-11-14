package ru.phyllosedis.judgeapi.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
public class PasswordHasher {
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    public String hash(String rawPassword) {
        return passwordHash.generate(rawPassword.toCharArray());
    }

    public boolean verify(String rawPassword, String storedHash) {
        return passwordHash.verify(rawPassword.toCharArray(), storedHash);
    }
}
