package ru.phyllosedis.judgedomain.dto.script;

public record ScriptParameter(
        String name,
        String type,
        String defaultValue,
        boolean required

) {
}
