package com.ezdrive.ezdrive.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.api.dto.AnswerResultDto;
import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.QuestionFeedbackDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Entities.TriviaGame;
import com.ezdrive.ezdrive.persistence.Repositories.GameSessionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.TriviaGameRepository;

// Service for handling trivia games
@Service
public class TriviaGameService {

    @Autowired
    private TriviaGameRepository triviaGameRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> generateQuestionsForTriviaSession(Long sessionId, String category) {
        List<Question> questions = questionRepository.findRandom10ByCategoryForTrivia(category);

        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        for (Question q : questions) {
            TriviaGame answer = new TriviaGame();
            answer.setGameSession(session);
            answer.setQuestion(q);
            triviaGameRepository.save(answer);
        }

        return questions;
    }

//save answer for question
    public AnswerResultDto  submitAnswer(Long sessionId, Long questionId, int selectedAnswer) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        TriviaGame gameAnswer = triviaGameRepository.findByGameSessionAndQuestion(session, question)
                .orElseThrow(() -> new RuntimeException("Answer entry not found"));

        boolean isCorrect = (selectedAnswer == question.getCorrectAnswer());

        gameAnswer.setSelectedAnswer(selectedAnswer);
        gameAnswer.setCorrect(selectedAnswer == question.getCorrectAnswer());
        gameAnswer.setAnsweredAt(LocalDateTime.now());

        triviaGameRepository.save(gameAnswer);

        return new AnswerResultDto(question.getCorrectAnswer(), isCorrect);
    }

    // calculate and return game result
    public GameResultResponseDto getTriviaGameResult(Long sessionId) {
        int numberOfCorrectAnswers = triviaGameRepository.countCorrectAnswers(sessionId);
        int score = (numberOfCorrectAnswers * 100) / 10;

        GameSession session = gameSessionRepository.findById(sessionId)
        .orElseThrow(() -> new RuntimeException("Session not found"));

        //update session score
        session.setScore(score);
        gameSessionRepository.save(session);
        

        Duration duration = Duration.between(session.getPlayedAt(), LocalDateTime.now());
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        String totalTimeFormatted = String.format("%02d:%02d", minutes, seconds);

        return new GameResultResponseDto(score, numberOfCorrectAnswers, totalTimeFormatted);
    }

    // Retrieves feedback for a specific game session
    public List<QuestionFeedbackDto> getSessionFeedback(Long sessionId)
    {
        List<TriviaGame> answers = triviaGameRepository.findByGameSessionId(sessionId);

        return answers.stream().map(answer -> {
        Question q = answer.getQuestion();

        String userAnswerText;
        if( answer.getSelectedAnswer() != null)
        {
            userAnswerText = getAnswerText(q, answer.getSelectedAnswer());
        }
        else
        {
            userAnswerText = "לא נבחרה תשובה.";
        }
        String correctAnswerText = getAnswerText(q, q.getCorrectAnswer());

        return new QuestionFeedbackDto(
            q.getQuestionText(),
            userAnswerText,
            correctAnswerText,
            answer.isCorrect(),
            q.getImageUrl()
        );
        }).collect(Collectors.toList());
    }

    // Retrieves the text for a specific answer option
    private String getAnswerText(Question question, int index) {
        return switch (index) {
            case 1 -> question.getAnswer1();
            case 2 -> question.getAnswer2();
            case 3 -> question.getAnswer3();
            case 4 -> question.getAnswer4();
            default -> "";
        };
    }
}

