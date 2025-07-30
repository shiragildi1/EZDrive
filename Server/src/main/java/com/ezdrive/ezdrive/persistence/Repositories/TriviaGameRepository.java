package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Entities.TriviaGame;

@Repository
public interface TriviaGameRepository extends JpaRepository<TriviaGame, Long> {
    Optional<TriviaGame> findByGameSessionAndQuestion(GameSession session, Question question);

    List<TriviaGame> findByGameSessionId(Long sessionId);

   @Query("SELECT COUNT(t) FROM TriviaGame t WHERE t.gameSession.id = :sessionId AND t.isCorrect = true")
    int countCorrectAnswers(@Param("sessionId") Long sessionId);
}