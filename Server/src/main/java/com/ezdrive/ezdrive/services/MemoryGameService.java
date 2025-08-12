package com.ezdrive.ezdrive.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.api.dto.MemoryGameResultResponseDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.MemoryGame;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Repositories.GameSessionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.MemoryGameRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;

@Service
public class MemoryGameService {

    @Autowired
    private MemoryGameRepository memoryGameRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // שלב 1: יצירת 10 שאלות רנדומליות ושיוך לסשן
    public List<Question> generateQuestionsForMemorySession(Long sessionId, String category) {
        List<Question> questions = questionRepository.findRandom10ByCategoryForMemory(category);
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Integer> questionCardPositions = IntStream.range(0,12)
        .boxed()
        .collect(Collectors.toList());
        Collections.shuffle(questionCardPositions); // Randomize the card order

        List<Integer> answerCardPositions = IntStream.range(0,12)
        .boxed()
        .collect(Collectors.toList());
        Collections.shuffle(answerCardPositions); 

        for (Question q : questions) {
            MemoryGame answer = new MemoryGame();
            answer.setGameSession(session);
            answer.setQuestion(q);
            
            // Randomly assign a question card and an answer card
            int questionCard = questionCardPositions.remove(0); // First available position
            int answerCard = answerCardPositions.remove(0);   // Second available position

            answer.setQuestionCard(questionCard);
            answer.setAnswerCard(answerCard);
            answer.setFlipped(false);
            memoryGameRepository.save(answer);
        }

        return questions;
    }

    
    // שלב 2: בדיקה האם הזוג נכון
    public boolean checkAnswer(Long sessionId, String userEmail, int selectedQuestionCard, int selectedAnswerCard) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        MemoryGame gameAnswer = memoryGameRepository.findByGameSessionAndQuestionCard(session.getId(), selectedQuestionCard)
                .orElseThrow(() -> new RuntimeException("Answer entry not found"));

        if(gameAnswer.getAnswerCard() == selectedAnswerCard)
        {
            gameAnswer.setFlipped(true);
            gameAnswer.setPlayerAnsweredEmail(userEmail);
            gameAnswer.setAnsweredAt(LocalDateTime.now());
        }

        memoryGameRepository.save(gameAnswer);
        return gameAnswer.getAnswerCard() == selectedAnswerCard;
    }

    //שלב 3: חישוב תוצאה סופית
    public MemoryGameResultResponseDto getGameResultMemory(Long sessionId) {

        int numberOfCorrectAnswers = memoryGameRepository.countCorrectAnswers(sessionId);
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

        // Get all answers for the session
        List<MemoryGame> answers = memoryGameRepository.findByGameSessionId(sessionId);

        int total = answers.size();
        int correctPlayer1 = (int) answers.stream().filter(answer -> "pessyisraeli@gmail.com".equals(answer.getPlayerAnsweredEmail())).count();
        int correctPlayer2 = (int) answers.stream().filter(answer -> "shiragiladi1@gmail.com".equals(answer.getPlayerAnsweredEmail())).count();
        return new MemoryGameResultResponseDto(total, correctPlayer1, correctPlayer2, totalTimeFormatted, score);
    }
}