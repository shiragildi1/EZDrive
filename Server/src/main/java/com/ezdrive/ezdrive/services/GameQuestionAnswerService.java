package com.ezdrive.ezdrive.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.persistence.Entities.GameQuestionAnswer;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Repositories.GameQuestionAnswerRepository;
import com.ezdrive.ezdrive.persistence.Repositories.GameSessionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;

@Service
public class GameQuestionAnswerService {

    @Autowired
    private GameQuestionAnswerRepository gameQuestionAnswerRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // שלב 1: יצירת 10 שאלות רנדומליות ושיוך לסשן
    public List<Question> generateQuestionsForSession(Long sessionId, String category) {
        List<Question> questions = questionRepository.findRandom10ByCategory(category);

        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        for (Question q : questions) {
            GameQuestionAnswer answer = new GameQuestionAnswer();
            answer.setGameSession(session);
            answer.setQuestion(q);
            // selectedAnswer = null, לא ענה עדיין
            gameQuestionAnswerRepository.save(answer);
        }

        return questions;
    }

    // שלב 2: שמירת תשובה לשאלה
    public void submitAnswer(Long sessionId, Long questionId, int selectedAnswer) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        GameQuestionAnswer gameAnswer = gameQuestionAnswerRepository.findByGameSessionAndQuestion(session, question)
                .orElseThrow(() -> new RuntimeException("Answer entry not found"));

        gameAnswer.setSelectedAnswer(selectedAnswer);
        gameAnswer.setCorrect(selectedAnswer == question.getCorrectAnswer());
        gameAnswer.setAnsweredAt(LocalDateTime.now());

        gameQuestionAnswerRepository.save(gameAnswer);
    }

    // שלב 3: חישוב תוצאה סופית
    public GameResultResponseDto getGameResult(Long sessionId) {
        List<GameQuestionAnswer> answers = gameQuestionAnswerRepository.findByGameSessionId(sessionId);

        int total = answers.size();
        int correct = (int) answers.stream().filter(GameQuestionAnswer::isCorrect).count();
        int score = correct * 100 / total;

        return new GameResultResponseDto(total, correct, score);
    }
}
