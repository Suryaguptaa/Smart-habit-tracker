package com.example.SmartHabbitTracker.repository;

import com.example.SmartHabbitTracker.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {


    Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date);


    List<HabitLog> findByHabitIdOrderByDateDesc(Long habitId);
    Long countByHabitId(Long habitId);
}