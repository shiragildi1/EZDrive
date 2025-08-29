package com.ezdrive.ezdrive.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.persistence.Repositories.ProfileRepository;

// Service for handling user profiles
@Service
public class ProfileService {
     @Autowired
    private ProfileRepository profileRepository;

    // This method retrieves game counts for a specific user and time range.
    public Map<String, Long> getGameCounts(String userEmail, String range) 
    {
        // Implementation to retrieve game counts for the user
        List<Object[]> rows;
        String week = "7";
        String month = "30";

        if (range == null || range.equalsIgnoreCase("all")) 
        {
            rows = profileRepository.countByGameTypeAllTime(userEmail);
        } 
        else if (week.equals(range)) 
        {
            rows = profileRepository.countByGameTypeLast7Days(userEmail, LocalDateTime.now().minusDays(7));
        } 
        else if (month.equals(range)) 
        {
            rows = profileRepository.countByGameTypeLast30Days(userEmail, LocalDateTime.now().minusDays(30));
        } 
        else 
        {
            rows = profileRepository.countByGameTypeAllTime(userEmail);
        }
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : rows) {
            String gameType = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            result.put(gameType, count);
        }
        return result;
    }

    // Retrieves game average scores for a specific user and time range.
    public Map<String, Long> getGameAverage(String userEmail, String range)
    {
        // Implementation to retrieve game counts for the user
        List<Object[]> rows;
                String week = "7";
        String month = "30";

        if (range == null || range.equalsIgnoreCase("all")) 
        {
            rows = profileRepository.findAverageScoreByGameType(userEmail);
        } 
        else if (week.equals(range)) 
        {
            rows = profileRepository.findAverageScoreByGameTypeLast7Days(userEmail, LocalDateTime.now().minusDays(7));
        } 
        else if (month.equals(range)) 
        {
            rows = profileRepository.findAverageScoreByGameTypeLast30Days(userEmail, LocalDateTime.now().minusDays(30));
        } 
        else 
        {
            rows = profileRepository.findAverageScoreByGameType(userEmail);
        }

        Map<String, Long> result = new HashMap<>();
        for (Object[] row : rows) {
            String gameType = (String) row[0];
            Long average = ((Number) row[1]).longValue();
            result.put(gameType, average);
        }
        return result;
    }
}
