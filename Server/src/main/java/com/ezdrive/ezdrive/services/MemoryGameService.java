package com.ezdrive.ezdrive.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean checkAnswer(Long sessionId, String userEmail, String currentPlayer, int selectedQuestionCard, int selectedAnswerCard) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        MemoryGame gameAnswer = memoryGameRepository.findByGameSessionAndQuestionCard(session.getId(), selectedQuestionCard)
                .orElseThrow(() -> new RuntimeException("Answer entry not found"));

        if(userEmail.equals(currentPlayer))
        {
            if(gameAnswer.getAnswerCard() == selectedAnswerCard)
            {
                gameAnswer.setFlipped(true);
                gameAnswer.setPlayerAnsweredEmail(userEmail);
                gameAnswer.setAnsweredAt(LocalDateTime.now());
            }
            memoryGameRepository.save(gameAnswer);
        }
        return gameAnswer.getAnswerCard() == selectedAnswerCard;
    }

    //שלב 3: חישוב תוצאה סופית
    public MemoryGameResultResponseDto getGameResultMemory(Long sessionId,  Map<String, Integer> scores) {
        GameSession session = gameSessionRepository.findById(sessionId)
        .orElseThrow(() -> new RuntimeException("Session not found"));

        String userEmail1 = session.getUser().getEmail();
        String userEmail2 = session.getUser2().getEmail();

        int score1 = scores.getOrDefault(userEmail1, 0);
        int score2 = scores.getOrDefault(userEmail2, 0);

        int totalPairs = 12;

        int percentP1 = (int) Math.round((score1 * 100.0) / totalPairs);
        int percentP2 = (int) Math.round((score2 * 100.0) / totalPairs);

        session.setScore(percentP1);
        session.setScore2(percentP2);
        gameSessionRepository.save(session);

        Map<String, Integer> percentMap = new HashMap<>();
        percentMap.put(userEmail1, percentP1);
        percentMap.put(userEmail2, percentP2);

        return new MemoryGameResultResponseDto(percentMap);
    }
}