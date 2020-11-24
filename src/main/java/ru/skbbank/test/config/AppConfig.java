package ru.skbbank.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private int classNumber;
    private int fio;
    private int subject;
    private int estimation;
    private int dateReceive;
    private String separator;
}
