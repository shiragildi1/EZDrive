package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
public interface RankingRepository extends JpaRepository<GameSession, Long> {
   @Query(value = """
        SELECT t.email AS email, SUM(t.score) AS totalScore, u.picture AS profileImage
        FROM (
            SELECT g.user_email  AS email, g.score  AS score
            FROM game_sessions g
            WHERE g.user_email  IS NOT NULL
            AND g.score       IS NOT NULL

            UNION ALL

            SELECT g.user_email2 AS email, g.score2 AS score
            FROM game_sessions g
            WHERE g.user_email2 IS NOT NULL
            AND g.score2      IS NOT NULL
        ) t
        LEFT JOIN users u ON t.email = u.email_id
        GROUP BY t.email, u.picture
        ORDER BY totalScore DESC
        """, nativeQuery = true)
    List<Object[]> findAllUsersTotalScores();    
}
