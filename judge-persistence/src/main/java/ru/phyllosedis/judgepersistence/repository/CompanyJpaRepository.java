package ru.phyllosedis.judgepersistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.phyllosedis.judgedomain.dto.company.Company;
import ru.phyllosedis.judgepersistence.entity.CompanyEntity;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class CompanyJpaRepository {
    @PersistenceContext(unitName = "judge-pu")
    private EntityManager em;

    @Transactional
    public Company save(Company company) {
        CompanyEntity entity = toEntity(company);
        if (Objects.isNull(entity.getId())) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return toDomain(entity);
    }

    public Optional<Company> findByName(String companyName) {
        return em.createQuery("SELECT c FROM CompanyEntity c WHERE c.name = :name", CompanyEntity.class)
                .setParameter("name", companyName)
                .getResultStream()
                .findFirst()
                .map(this::toDomain);
    }

    public Optional<Company> findByApiKey(String apiKey) {
        return em.createQuery("SELECT c FROM CompanyEntity c WHERE c.apiKey = :apiKey", CompanyEntity.class)
                .setParameter("apiKey", apiKey)
                .getResultStream()
                .findFirst()
                .map(this::toDomain);
    }

    protected CompanyEntity toEntity(Company company) {
        return new CompanyEntity(company.id(), company.name(), company.passwordHash(), company.apiKey());
    }

    protected Company toDomain(CompanyEntity company) {
        return new Company(company.getId(), company.getName(), company.getPasswordHash(), company.getApiKey());
    }
}
