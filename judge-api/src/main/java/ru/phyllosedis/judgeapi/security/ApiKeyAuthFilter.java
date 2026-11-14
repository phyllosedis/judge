package ru.phyllosedis.judgeapi.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import ru.phyllosedis.judgeapi.service.CurrentCompanyContext;
import ru.phyllosedis.judgepersistence.repository.CompanyJpaRepository;

import java.io.IOException;

@Provider
@ApiKeyProtected
@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    @Inject
    private CurrentCompanyContext companyContext;

    @Inject
    private CompanyJpaRepository companyRepository;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String apiKey = ctx.getHeaderString("X-API-Key");
        if (apiKey == null || apiKey.isBlank()) {
            ctx.abortWith(Response.status(401).entity("Missing X-API-Key").build());
            return;
        }

        companyRepository.findByApiKey(apiKey).ifPresentOrElse(
                companyContext::setCompany,
                () -> ctx.abortWith(Response.status(401).entity("Invalid X-API-Key").build())
        );
    }
}
