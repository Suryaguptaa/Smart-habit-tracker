package com.example.SmartHabbitTracker.controller;

import com.example.SmartHabbitTracker.dto.HabitRequest;
import com.example.SmartHabbitTracker.dto.HabitLogRequest;
import com.example.SmartHabbitTracker.dto.HabitResponse;
import com.example.SmartHabbitTracker.model.Habit;
import com.example.SmartHabbitTracker.model.HabitLog;
import com.example.SmartHabbitTracker.service.HabitService;
import com.example.SmartHabbitTracker.service.GeminiService;
import com.example.SmartHabbitTracker.dto.AnalyticsSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "http://localhost:5173")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @Autowired
    private GeminiService geminiService;


    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@RequestBody HabitRequest request) {
        return ResponseEntity.ok(new HabitResponse(habitService.createHabit(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitResponse>> getHabits(@PathVariable Long userId) {
        List<HabitResponse> habits = habitService.getHabitsByUserId(userId)
                .stream()
                .map(HabitResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(habits);
    }

    @PostMapping("/{habitId}/log")
    public ResponseEntity<HabitLog> logHabit(
            @PathVariable Long habitId,
            @RequestBody HabitLogRequest request) {
        return ResponseEntity.ok(habitService.logHabit(habitId, request));
    }

    @GetMapping("/{habitId}/streak")
    public ResponseEntity<Integer> getStreak(@PathVariable Long habitId) {
        return ResponseEntity.ok(habitService.calculateStreak(habitId));
    }

    @GetMapping("/{habitId}/analytics")
    public ResponseEntity<AnalyticsSummary> getAnalytics(@PathVariable Long habitId) {
        return ResponseEntity.ok(habitService.getHabitAnalytics(habitId));
    }

    @GetMapping("/{habitId}/ai-tip")
    public ResponseEntity<Map<String, String>> getAiTip(@PathVariable Long habitId) {
        Habit habit = habitService.getHabitById(habitId);
        int streak = habitService.calculateStreak(habitId);
        String tip = geminiService.getHealthTip(habit.getName(), streak);

        Map<String, String> result = new LinkedHashMap<>();
        result.put("habit", habit.getName());
        result.put("streak", streak + " days");
        result.put("ai_tip", tip);
        return ResponseEntity.ok(result);
    }
}
