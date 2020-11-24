package ru.skbbank.test.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skbbank.test.config.AppConfig;
import ru.skbbank.test.entity.SchoolClass;
import ru.skbbank.test.repository.SchoolClassRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileLoadServiceImpl implements LoadService {

    private final SchoolClassRepository schoolClassRepository;
    private final AppConfig appConfig;

    /**
     * Метод обрабатывает TXT файл и сохраняет данные в БД, если такой записи нет
     */
    @Override
    public boolean upload(MultipartFile file) {
        if (file.isEmpty())
            return false;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                processLine(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            log.error("Ошибка при чтении файла.", e);
        }
        return false;
    }

    private void processLine(String line) {
        String[] split = line.split(appConfig.getSeparator());
        Field field = fill(split);
        if (notExistSchoolClass(field)) {
            save(field);
        }
    }

    private Field fill(String[] split) {
        Field field = new Field();
        field.setClassNumber(Integer.valueOf(split[appConfig.getClassNumber()]));
        field.setFio(split[appConfig.getFio()]);
        field.setSubject(split[appConfig.getSubject()]);
        field.setEstimation(Integer.valueOf(split[appConfig.getEstimation()]));
        field.setDateTime(LocalDateTime.parse(split[appConfig.getDateReceive()], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return field;
    }

    private boolean notExistSchoolClass(Field field) {
        Optional<SchoolClass> optionalSchoolClass =
                schoolClassRepository.findByClassNumberAndFioAndSubjectAndEstimationAndDateReceive(
                        field.getClassNumber(), field.getFio(), field.getSubject(), field.getEstimation(), field.getDateTime()
                );
        return optionalSchoolClass.isEmpty();
    }

    private void save(Field field) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassNumber(field.getClassNumber());
        schoolClass.setFio(field.getFio());
        schoolClass.setSubject(field.getSubject());
        schoolClass.setEstimation(field.getEstimation());
        schoolClass.setDateReceive(field.getDateTime());
        schoolClassRepository.save(schoolClass);
    }

    @Data
    public static class Field {
        private Integer classNumber;
        private String fio;
        private String subject;
        private Integer estimation;
        private LocalDateTime dateTime;
    }
}
