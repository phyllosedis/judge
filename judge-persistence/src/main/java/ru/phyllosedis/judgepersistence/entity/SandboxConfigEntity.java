package ru.phyllosedis.judgepersistence.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.phyllosedis.judgedomain.Language;
import ru.phyllosedis.judgedomain.limit.ResourceLimits;
import ru.phyllosedis.judgepersistence.converter.ResourceLimitsConverter;

import java.util.UUID;

@Entity
@Table(name = "sandbox_configs")
@Getter
@Setter
@AllArgsConstructor
public class SandboxConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Convert(converter = ResourceLimitsConverter.class)
    @Column(name = "limits", columnDefinition = "jsonb", nullable = false)
    private ResourceLimits limits;

    protected SandboxConfigEntity() {
    }

    public SandboxConfigEntity(UUID companyId, String name, Language language, ResourceLimits limits) {
        this.companyId = companyId;
        this.name = name;
        this.language = language;
        this.limits = limits;
    }
}
