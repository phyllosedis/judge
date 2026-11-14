package ru.phyllosedis.judgeapi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import ru.phyllosedis.judgeapi.security.ApiKeyGenerator;
import ru.phyllosedis.judgeapi.security.PasswordHasher;
import ru.phyllosedis.judgedomain.dto.company.AuthResponse;
import ru.phyllosedis.judgedomain.dto.company.Company;
import ru.phyllosedis.judgedomain.dto.sandbox.RegisterCompanyRequest;
import ru.phyllosedis.judgepersistence.repository.CompanyJpaRepository;

@ApplicationScoped
public class CompanyService {
    @Inject
    private CompanyJpaRepository companyJpaRepository;

    @Inject
    private PasswordHasher passwordHasher;

    @Inject
    private ApiKeyGenerator apiKeyGenerator;


    public AuthResponse register(RegisterCompanyRequest request) {
        String passwordHash = passwordHasher.hash(request.password());
        String apiKey = apiKeyGenerator.generate();

        Company toSave = new Company(null, request.name(), passwordHash, apiKey);

        Company saved = companyJpaRepository.save(toSave);

        return new AuthResponse(saved.id(), saved.name(), saved.apiKey());
    }

    public AuthResponse login(RegisterCompanyRequest request) {
        Company company = companyJpaRepository.findByName(request.name())
                .orElseThrow(() -> new NotAuthorizedException("Invalid credentials"));

        if (!passwordHasher.verify(request.password(), company.passwordHash())) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        return new AuthResponse(company.id(), company.name(), company.apiKey());
    }

}
