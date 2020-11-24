package ru.skbbank.test.service;

import ru.skbbank.test.controller.dto.AverageGradeDto;

import java.util.List;

public interface SchoolClassService {
    List<AverageGradeDto> getAverageGrade(Integer numberClass);
    List<AverageGradeDto> getAverageGradeFromSql(Integer numberClass);
}
