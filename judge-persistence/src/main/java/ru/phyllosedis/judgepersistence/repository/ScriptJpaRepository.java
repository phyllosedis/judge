package ru.phyllosedis.judgepersistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.phyllosedis.judgedomain.dto.script.Script;
import ru.phyllosedis.judgepersistence.entity.ScriptEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ScriptJpaRepository {
    @PersistenceContext(unitName = "judge-pu")
    private EntityManager em;

    @Transactional
    public Script save(Script script) {
        ScriptEntity entity = new ScriptEntity(
                null,
                script.companyId(),
                script.sandboxId(),
                script.name(),
                script.sourceCode(),
                script.inputSchema(),
                script.version()
        );
        em.persist(entity);
        return toDomain(entity);
    }

    public List<Script> findAllByCompanyId(UUID companyId) {
        return em.createQuery("SELECT s FROM ScriptEntity s WHERE s.companyId = :companyId", ScriptEntity.class)
                .setParameter("companyId", companyId)
                .getResultList()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<Script> findByIdAndCompanyId(UUID id, UUID companyId) {
        return em.createQuery(
                        "SELECT s FROM ScriptEntity s WHERE s.id = :id AND s.companyId = :companyId",
                        ScriptEntity.class)
                .setParameter("id", id)
                .setParameter("companyId", companyId)
                .getResultStream()
                .findFirst()
                .map(this::toDomain);
    }

    private Script toDomain(ScriptEntity e) {
        return new Script(
                e.getId(),
                e.getCompanyId(),
                e.getSandboxId(),
                e.getName(),
                e.getSourceCode(),
                e.getInputSchema(),
                e.getVersion()
        );
    }
}
