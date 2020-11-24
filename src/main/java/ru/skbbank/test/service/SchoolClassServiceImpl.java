package ru.skbbank.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import ru.skbbank.test.controller.dto.AverageGradeDto;
import ru.skbbank.test.entity.SchoolClass;
import ru.skbbank.test.repository.SchoolClassRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Value("classpath:sql/averageGrade.sql")
    private Resource unloadOrders;

    /**
     * Получение среднего балла учеников по месяцам, используя Stream API
     * @param numberClass   номер класса
     * @return Коллекция объектов {@link AverageGradeDto}
     */
    @Override
    public List<AverageGradeDto> getAverageGrade(Integer numberClass) {
        List<AverageGradeDto> averageGrades = new ArrayList<>();

        schoolClassRepository.findAll().stream()
                .filter(schoolClass -> schoolClass.getClassNumber().compareTo(numberClass) == 0)
                .sorted(Comparator.comparing(SchoolClass::getFio))
                .collect(Collectors.groupingBy(SchoolClass::getFio))
                .forEach((key, value) -> value.stream()
                        .collect(Collectors.groupingBy(
                                SchoolClass::getMonth,
                                Collectors.mapping(SchoolClass::getEstimation, Collectors.summarizingInt(Integer::intValue))))
                        .forEach((key1, value1) -> {
                            AverageGradeDto averageGradeDto = new AverageGradeDto();
                            averageGradeDto.setFio(key);
                            averageGradeDto.setMonth(key1);
                            averageGradeDto.setAverage(value1.getAverage());
                            averageGrades.add(averageGradeDto);
                        }));

        return averageGrades;
    }

    /**
     * Получение среднего балла учеников по месяцам, используя SQL запрос
     * @param numberClass   номер класса
     * @return Коллекция объектов {@link AverageGradeDto}
     */
    @Override
    public List<AverageGradeDto> getAverageGradeFromSql(Integer numberClass) {
        List<AverageGradeDto> averageGrades = new ArrayList<>();
        String sqlQuery = resourceSql(unloadOrders);
        namedJdbcTemplate.query(
                sqlQuery,
                new MapSqlParameterSource().addValue("numb", numberClass),
                (ResultSet resultSet) -> processRecordSet(resultSet, averageGrades)
        );
        return averageGrades;
    }

    private String resourceSql(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void processRecordSet(ResultSet rs, List<AverageGradeDto> averageGrades) {
        try {
            do {
                AverageGradeDto averageGrade = new AverageGradeDto();
                averageGrade.setFio(rs.getString("fio"));
                averageGrade.setMonth(rs.getInt("month"));
                averageGrade.setAverage(rs.getDouble("average"));
                averageGrades.add(averageGrade);
            } while (rs.next());
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
