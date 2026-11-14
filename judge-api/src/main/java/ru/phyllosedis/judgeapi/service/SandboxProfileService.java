package ru.phyllosedis.judgeapi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.phyllosedis.judgedomain.dto.sandbox.RegisterSandboxRequest;
import ru.phyllosedis.judgedomain.dto.sandbox.SandboxProfile;
import ru.phyllosedis.judgedomain.limit.ResourceLimits;
import ru.phyllosedis.judgepersistence.repository.SandboxConfigJpaRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SandboxProfileService {

    @Inject
    private SandboxConfigJpaRepository sandboxRepository;

    @Inject
    private CurrentCompanyContext companyContext;

    public SandboxProfile register(RegisterSandboxRequest request) {
        UUID companyId = companyContext.getCompany().id();
        ResourceLimits limits = request.limits() != null ? request.limits() : ResourceLimits.defaults();

        SandboxProfile toSave = new SandboxProfile(null, companyId, request.name(), request.language(), limits);
        return sandboxRepository.save(toSave);
    }

    public List<SandboxProfile> getCompanySandboxes() {
        UUID companyId = companyContext.getCompany().id();
        return sandboxRepository.findAllByCompanyId(companyId);
    }
}
