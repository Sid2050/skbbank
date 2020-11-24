package ru.skbbank.test.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "classes")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer classNumber;
    private String fio;
    private String subject;
    private Integer estimation;
    private LocalDateTime dateReceive;

    public Integer getMonth() {
        if (dateReceive != null)
            return dateReceive.getMonthValue();
        return null;
    }
}