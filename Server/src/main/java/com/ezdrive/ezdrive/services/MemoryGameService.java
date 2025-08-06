package com.ezdrive.ezdrive.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String correctAnswer;
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Integer> cardPositions = IntStream.range(0,24)
        .boxed()
        .collect(Collectors.toList());
        Collections.shuffle(cardPositions); // Randomize the card order



        for (Question q : questions) {
            MemoryGame answer = new MemoryGame();//////////////////
            answer.setGameSession(session);
            answer.setQuestion(q);
            
            // Randomly assign a question card and an answer card
            int questionCard = cardPositions.remove(0); // First available position
            int answerCard = cardPositions.remove(0);   // Second available position

            answer.setQuestionCard(questionCard);
            answer.setAnswerCard(answerCard);
            answer.setFlipped(false);
            memoryGameRepository.save(answer);
        }

        return questions;
    }

    
    // שלב 2: בדיקה האם הזוג נכון
    public boolean checkAnswer(Long sessionId, String userEmail, int selectedQuestionCard, int selectedAnswerCard) {
         System.out.println("From client: "+userEmail+" "+selectedQuestionCard);
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
       
        System.out.println(gameAnswer.isFlipped());
        return gameAnswer.getAnswerCard() == selectedAnswerCard;
    }

    // //שלב 3: חישוב תוצאה סופית
    // public MemoryGameResultResponseDto GamgetGameResult(Long sessionId) {
    //     List<MemoryGame> answers = memoryGameRepository.findByGameSessionId(sessionId);

    //     int total = answers.size();
    //     int correctPlayer1 = (int) answers.stream().filter(answer -> "pessyisraeli@gmail.com".equals(answer.getPlayerAnswered().getEmail())).count();
    //     int correctPlayer2 = (int) answers.stream().filter(answer -> "shiragiladi1@gmail.com".equals(answer.getPlayerAnswered().getEmail())).count();
    //     //int score = correct * 100 / total;

    //     return new MemoryGameResultResponseDto(total, correctPlayer1, correctPlayer2);
    // }
}