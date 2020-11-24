package ru.skbbank.test.service;

import org.springframework.web.multipart.MultipartFile;

public interface LoadService {
    boolean upload(MultipartFile file);
}
