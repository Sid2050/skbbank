package ru.skbbank.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skbbank.test.entity.SchoolClass;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByClassNumberAndFioAndSubjectAndEstimationAndDateReceive(
            Integer classNumber, String fio, String subject, Integer estimation, LocalDateTime dateReceive
    );
}
