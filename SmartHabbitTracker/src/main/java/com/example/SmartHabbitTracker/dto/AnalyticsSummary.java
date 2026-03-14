package com.example.SmartHabbitTracker.dto;

public class AnalyticsSummary {
    private int currentStreak;
    private long totalCompleted;
    private String consistencyMessage; // e.g., "Great job!"


    public AnalyticsSummary(int currentStreak, long totalCompleted, String consistencyMessage) {
        this.currentStreak = currentStreak;
        this.totalCompleted = totalCompleted;
        this.consistencyMessage = consistencyMessage;
    }


    public int getCurrentStreak() { return currentStreak; }
    public long getTotalCompleted() { return totalCompleted; }
    public String getConsistencyMessage() { return consistencyMessage; }
}