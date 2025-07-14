package com.ezdrive.ezdrive.persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezdrive.ezdrive.persistence.Entities.GameQuestionAnswer;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameQuestionAnswerRepository extends JpaRepository<GameQuestionAnswer, Long> {
    Optional<GameQuestionAnswer> findByGameSessionAndQuestion(GameSession session, Question question);

    List<GameQuestionAnswer> findByGameSessionId(Long sessionId);
}