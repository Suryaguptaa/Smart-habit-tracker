package com.example.SmartHabbitTracker.controller;

import com.example.SmartHabbitTracker.dto.HabitRequest;
import com.example.SmartHabbitTracker.dto.HabitLogRequest;
import com.example.SmartHabbitTracker.model.Habit;
import com.example.SmartHabbitTracker.model.HabitLog;
import com.example.SmartHabbitTracker.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.SmartHabbitTracker.dto.AnalyticsSummary;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "http://localhost:5173")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @PostMapping
    public ResponseEntity<Habit> createHabit(@RequestBody HabitRequest request) {
        return ResponseEntity.ok(habitService.createHabit(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Habit>> getHabits(@PathVariable Long userId) {
        return ResponseEntity.ok(habitService.getHabitsByUserId(userId));
    }

    @PostMapping("/{habitId}/log")
    public ResponseEntity<HabitLog> logHabit(@PathVariable Long habitId, @RequestBody HabitLogRequest request) {
        return ResponseEntity.ok(habitService.logHabit(habitId, request));
    }

    @GetMapping("/{habitId}/streak")
    public ResponseEntity<Integer> getStreak(@PathVariable Long habitId) {
        int streak = habitService.calculateStreak(habitId);
        return ResponseEntity.ok(streak);
    }

    @GetMapping("/{habitId}/analytics")
    public ResponseEntity<AnalyticsSummary> getAnalytics(@PathVariable Long habitId) {
        return ResponseEntity.ok(habitService.getHabitAnalytics(habitId));
    }
}