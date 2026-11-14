package ru.phyllosedis.judgedomain.dto.script;

import java.util.List;
import java.util.UUID;

public record Script(
        UUID id,
        UUID companyId,
        UUID sandboxId,
        String name,
        String sourceCode,
        List<ScriptParameter> inputSchema,
        int version
) {
}
