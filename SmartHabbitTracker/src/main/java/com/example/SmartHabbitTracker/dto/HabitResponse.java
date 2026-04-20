package com.example.SmartHabbitTracker.dto;

import com.example.SmartHabbitTracker.model.Habit;
import java.time.LocalDateTime;

public class HabitResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;

    public HabitResponse(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.createdAt = habit.getCreatedAt();
        this.userId = habit.getUser().getId();
        this.userName = habit.getUser().getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
}