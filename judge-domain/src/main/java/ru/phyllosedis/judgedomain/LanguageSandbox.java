package ru.phyllosedis.judgedomain;

public interface LanguageSandbox {
    Language getLanguage();

    ExecutionContext execute(ExecutionContext context);

}
