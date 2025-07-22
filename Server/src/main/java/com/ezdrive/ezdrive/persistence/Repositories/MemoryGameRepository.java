package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.MemoryGame;
import com.ezdrive.ezdrive.persistence.Entities.Question;


@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGame, Long> {
    Optional<MemoryGame> findByGameSessionAndQuestion(GameSession session, Question question);

    List<MemoryGame> findByGameSessionId(Long sessionId);

    @Query(value = "SELECT question_card FROM memory_game WHERE question_id = :questionId and game_session_id = :gameSessionId", nativeQuery = true)
    int findQuestionCardPositionByGameSesstionAndQuestion(Long gameSessionId, Long questionId);

    @Query(value = "SELECT answer_card FROM memory_game WHERE question_id = :questionId and game_session_id = :gameSessionId", nativeQuery = true)
    int findAnswerCardPositionByGameSesstionAndQuestion(Long gameSessionId, Long questionId);
}