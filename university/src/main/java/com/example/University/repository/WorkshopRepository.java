package com.example.University.repository;

import com.example.University.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    List<Workshop> findByStatus(String status);

    List<Workshop> findByStatusAndStartDatetimeAfter(String status, LocalDateTime dateTime);
}