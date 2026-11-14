package ru.phyllosedis.judgepersistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.phyllosedis.judgedomain.dto.script.ScriptParameter;
import ru.phyllosedis.judgepersistence.converter.ScriptParameterListConverter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scripts")
public class ScriptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "sandbox_id", nullable = false)
    private UUID sandboxId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "source_code", nullable = false)
    private String sourceCode;

    @Convert(converter = ScriptParameterListConverter.class)
    @Column(name = "input_schema", columnDefinition = "jsonb", nullable = false)
    private List<ScriptParameter> inputSchema;

    @Column(name = "version", nullable = false)
    private int version;


}
