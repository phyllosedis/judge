package ru.phyllosedis.judgepersistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.phyllosedis.judgedomain.dto.sandbox.SandboxProfile;
import ru.phyllosedis.judgepersistence.entity.SandboxConfigEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SandboxConfigJpaRepository {
    @PersistenceContext(unitName = "judge-pu")
    private EntityManager em;

    @Transactional
    public SandboxProfile save(SandboxProfile profile) {
        SandboxConfigEntity entity = new SandboxConfigEntity(
                profile.companyId(),
                profile.name(),
                profile.language(),
                profile.limits()
        );
        em.persist(entity);
        return toDomain(entity);
    }

    public List<SandboxProfile> findAllByCompanyId(UUID companyId) {
        return em.createQuery(
                        "SELECT s FROM SandboxConfigEntity s WHERE s.companyId = :companyId",
                        SandboxConfigEntity.class)
                .setParameter("companyId", companyId)
                .getResultList()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private SandboxProfile toDomain(SandboxConfigEntity e) {
        return new SandboxProfile(e.getId(), e.getCompanyId(), e.getName(), e.getLanguage(), e.getLimits());
    }
}
