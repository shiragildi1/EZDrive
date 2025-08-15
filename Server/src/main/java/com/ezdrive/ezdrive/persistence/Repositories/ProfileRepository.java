package com.ezdrive.ezdrive.persistence.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;

// This interface is used to manage user profiles and retrieve game statistics.

public interface ProfileRepository  extends  JpaRepository<GameSession, Long> {
    //---------------------------------------------count-------------------------------------
    @Query("""
        SELECT g.gameType, COUNT(g)
        FROM GameSession g
        JOIN g.user  u
        LEFT JOIN g.user2 u2
        WHERE (u.email = :userEmail OR u2.email = :userEmail)
        GROUP BY g.gameType
        """)
        // This query counts the number of games played by type for a specific user.
        List<Object[]> countByGameTypeAllTime(@Param("userEmail") String userEmail);

        @Query("""
            SELECT g.gameType, COUNT(g)
            FROM GameSession g
            JOIN g.user  u
            LEFT JOIN g.user2 u2
            WHERE (u.email = :userEmail OR u2.email = :userEmail)
            AND g.playedAt >= :sevenDaysAgo
            GROUP BY g.gameType
        """)
        // This query counts the number of games played by type in the last 7 days for a specific user.
        List<Object[]> countByGameTypeLast7Days(@Param("userEmail") String email,
                                                @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

@Query("""
       SELECT g.gameType, COUNT(g)
            FROM GameSession g
            JOIN g.user  u
            LEFT JOIN g.user2 u2
            WHERE (u.email = :userEmail OR u2.email = :userEmail)
            AND g.playedAt >= :thirtyDaysAgo
            GROUP BY g.gameType
        """)
    // This query counts the number of games played by type in the last 30 days for a specific user.
    List<Object[]> countByGameTypeLast30Days(@Param("userEmail") String email,
                                            @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    //---------------------------------------------average-------------------------------------
    @Query("""
            SELECT g.gameType,
            AVG(
             CASE
               WHEN u.email  = :userEmail THEN g.score
               WHEN u2.email = :userEmail THEN g.score2
               ELSE NULL
             END
           )
            FROM GameSession g
            JOIN g.user u
            LEFT JOIN g.user2 u2
            WHERE (u.email = :userEmail OR u2.email = :userEmail)
            GROUP BY g.gameType
            """)
            // This query calculates the average score by game type for a specific user.
            List<Object[]> findAverageScoreByGameType(@Param("userEmail") String userEmail);

     @Query("""
            SELECT 
            g.gameType,
            AVG(
             CASE
               WHEN u.email  = :userEmail THEN g.score
               WHEN u2.email = :userEmail THEN g.score2
               ELSE NULL
             END
           )
            FROM GameSession g
            JOIN g.user u
            LEFT JOIN g.user2 u2
            WHERE (u.email = :userEmail OR u2.email = :userEmail) AND g.playedAt >= :sevenDaysAgo
            GROUP BY g.gameType
            """)
            // This query calculates the average score by game type for a specific user in the last 7 days.
            List<Object[]> findAverageScoreByGameTypeLast7Days(@Param("userEmail") String userEmail,
                                                             @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);


            @Query("""
            SELECT g.gameType,
            AVG(
             CASE
               WHEN u.email  = :userEmail THEN g.score
               WHEN u2.email = :userEmail THEN g.score2
               ELSE NULL
             END
           )
            FROM GameSession g
            JOIN g.user u
            LEFT JOIN g.user2 u2
            WHERE (u.email = :userEmail OR u2.email = :userEmail) AND g.playedAt >= :thirtyDaysAgo
            GROUP BY g.gameType
            """)
            // This query calculates the average score by game type for a specific user in the last 30 days.
            List<Object[]> findAverageScoreByGameTypeLast30Days(@Param("userEmail") String userEmail,
                                                             @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}

