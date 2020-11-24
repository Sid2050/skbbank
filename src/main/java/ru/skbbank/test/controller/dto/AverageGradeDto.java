package ru.skbbank.test.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Успеваемость студента")
public class AverageGradeDto {
    @ApiModelProperty("ФИО")
    private String fio;
    @ApiModelProperty("Год")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer year;
    @ApiModelProperty("Месяц")
    private Integer month;
    @ApiModelProperty("Средний балл")
    private Double average;
}
