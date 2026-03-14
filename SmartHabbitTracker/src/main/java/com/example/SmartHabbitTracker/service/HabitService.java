package com.example.SmartHabbitTracker.service;

import com.example.SmartHabbitTracker.dto.HabitRequest;
import com.example.SmartHabbitTracker.dto.HabitLogRequest;
import com.example.SmartHabbitTracker.model.Habit;
import com.example.SmartHabbitTracker.model.HabitLog;
import com.example.SmartHabbitTracker.model.User;
import com.example.SmartHabbitTracker.repository.HabitLogRepository;
import com.example.SmartHabbitTracker.repository.HabitRepository;
import com.example.SmartHabbitTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SmartHabbitTracker.dto.AnalyticsSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    public Habit createHabit(HabitRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setUser(user);

        return habitRepository.save(habit);
    }

    public List<Habit> getHabitsByUserId(Long userId) {
        return habitRepository.findByUserId(userId);
    }

    public HabitLog logHabit(Long habitId, HabitLogRequest request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndDate(habitId, request.getDate());
        if (existingLog.isPresent()) {
            throw new RuntimeException("Habit already tracked for this date!");
        }

        HabitLog log = new HabitLog();
        log.setHabit(habit);
        log.setDate(request.getDate());
        log.setStatus(request.getStatus());

        return habitLogRepository.save(log);
    }

    public int calculateStreak(Long habitId) {
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByDateDesc(habitId);

        if (logs.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        if (!logs.get(0).getDate().equals(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
            if (!logs.get(0).getDate().equals(expectedDate)) {
                return 0;
            }
        }

        for (HabitLog log : logs) {
            if (log.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }

    public AnalyticsSummary getHabitAnalytics(Long habitId) {

        int streak = calculateStreak(habitId);
        Long total = habitLogRepository.countByHabitId(habitId);


        String message = "Keep going!";
        if (streak > 5) message = "You are on fire! 🔥";
        if (streak > 20) message = "Unstoppable! 🚀";
        if (streak == 0) message = "Let's start fresh today! 💪";

        return new AnalyticsSummary(streak, total, message);

    }
}