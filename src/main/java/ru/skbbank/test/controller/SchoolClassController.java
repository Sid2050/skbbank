package ru.skbbank.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skbbank.test.controller.dto.AverageGradeDto;
import ru.skbbank.test.exceptions.ClassNotFoundException;
import ru.skbbank.test.service.LoadService;
import ru.skbbank.test.service.SchoolClassService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/classes")
@RequiredArgsConstructor
@Api("API для работы с классами")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;
    private final LoadService loadService;

    @ApiOperation(value = "Загрузить показатели студентов")
    @PostMapping
    public ResponseEntity<Void> saveStudentGrades(@RequestParam("file")MultipartFile file) {
        CompletableFuture.supplyAsync(() -> loadService.upload(file));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Получить средний балл по классу (Использует Stream API)")
    @GetMapping("/{numberClass}")
    public ResponseEntity<List<AverageGradeDto>> getAverageGrade(
            @ApiParam(value = "Номер класса", required = true) @PathVariable String numberClass) {
        List<AverageGradeDto> averageGrades = schoolClassService.getAverageGrade(Integer.valueOf(numberClass));
        if (CollectionUtils.isEmpty(averageGrades)) {
            throw new ClassNotFoundException();
        }
        return ResponseEntity.ok(averageGrades);
    }

    @ApiOperation(value = "Получить средний балл по классу за весь период (Использует SQL запрос)")
    @GetMapping("/sql/{numberClass}")
    public ResponseEntity<List<AverageGradeDto>> getAverageGradeSql(
            @ApiParam(value = "Номер класса", required = true) @PathVariable String numberClass) {
        List<AverageGradeDto> averageGrades = schoolClassService.getAverageGradeFromSql(Integer.valueOf(numberClass));
        if (CollectionUtils.isEmpty(averageGrades)) {
            throw new ClassNotFoundException();
        }
        return ResponseEntity.ok(averageGrades);
    }
}
