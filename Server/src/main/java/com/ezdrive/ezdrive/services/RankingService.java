package com.ezdrive.ezdrive.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.api.dto.RankingStatsDto;
import com.ezdrive.ezdrive.persistence.Repositories.RankingRepository;

@Service
public class RankingService {
    @Autowired
    private RankingRepository rankingRepository;

    public List<RankingStatsDto> getAllUsersTotalScores() {
        List<Object[]> rows = rankingRepository.findAllUsersTotalScores();
        List<RankingStatsDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            String email = (String) row[0];
            Long totalScore = ((Number) row[1]).longValue();
            String profileImage = row[2] != null ? row[2].toString() : null;
            result.add(new RankingStatsDto(email, totalScore, profileImage));
        }
        return result;
    }
}
