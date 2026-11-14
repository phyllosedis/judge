package ru.phyllosedis.judgedomain;

import java.util.List;

public record ExecutionContext(
        ExecutionResult executionResult,
        String sourceCode,
        List<EnvironmentExecutionVariable> variables
) {

}
