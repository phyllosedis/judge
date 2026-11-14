package ru.phyllosedis.judgeapi.service;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;
import ru.phyllosedis.judgedomain.dto.company.Company;

@Setter
@Getter
@RequestScoped
public class CurrentCompanyContext {
    private Company company;
}
