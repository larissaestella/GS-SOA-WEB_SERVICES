package br.com.nexuswork.service;

import org.springframework.stereotype.Service;

import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class GamificationService {

    private final NavigableMap<Integer, Integer> thresholds = new TreeMap<>();

    public GamificationService() {
        thresholds.put(1, 0);
        thresholds.put(2, 1000);
        thresholds.put(3, 2500);
        thresholds.put(4, 5000);
        thresholds.put(5, 10000);
    }

    public int levelFromPoints(int points) {
        int level = 1;
        for (var e : thresholds.entrySet()) {
            if (points >= e.getValue()) level = e.getKey();
            else break;
        }
        return level;
    }

    public int pointsToNextLevel(int points) {
        for (var e : thresholds.entrySet()) {
            if (points < e.getValue()) return e.getValue() - points;
        }
        return 0;
    }
}
