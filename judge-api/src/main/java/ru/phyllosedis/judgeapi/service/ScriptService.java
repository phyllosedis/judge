package ru.phyllosedis.judgeapi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.phyllosedis.judgedomain.dto.script.CreateScriptRequest;
import ru.phyllosedis.judgedomain.dto.script.Script;
import ru.phyllosedis.judgepersistence.repository.ScriptJpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ScriptService {
    @Inject
    private ScriptJpaRepository scriptRepository;

    @Inject
    private CurrentCompanyContext companyContext;

    public Script create(CreateScriptRequest request) {
        UUID companyId = companyContext.getCompany().id();
        var schema = request.inputSchema() != null ? request.inputSchema() : Collections.<ru.phyllosedis.judgedomain.dto.script.ScriptParameter>emptyList();

        Script toSave = new Script(
                null,
                companyId,
                request.sandboxId(),
                request.name(),
                request.sourceCode(),
                schema,
                1
        );
        return scriptRepository.save(toSave);
    }

    public List<Script> listMyScripts() {
        UUID companyId = companyContext.getCompany().id();
        return scriptRepository.findAllByCompanyId(companyId);
    }
}
