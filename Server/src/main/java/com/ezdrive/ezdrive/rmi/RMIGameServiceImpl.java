

package com.ezdrive.ezdrive.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.ezdrive.ezdrive.api.dto.MemoryGameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryQuestionDto;
import com.ezdrive.ezdrive.api.dto.MemoryStateDto;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Repositories.MemoryGameRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.services.MemoryGameService;


public class RMIGameServiceImpl extends UnicastRemoteObject implements RMIGameService {

    private final MemoryGameService memoryGame;
    private final MemoryGameRepository memoryGameRepository;
    private final QuestionRepository questionRepository;
    private final Map<Long, GameState> gameStates = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> gamePlayers = new ConcurrentHashMap<>();
    // שמירת רשימת השאלות שנוצרה בפעם הראשונה לכל sessionId
    private final Map<Long, List<Question>> sessionQuestions = new ConcurrentHashMap<>();

    public RMIGameServiceImpl(ApplicationContext springContext) throws RemoteException {
        super();
        this.memoryGame = springContext.getBean(MemoryGameService.class);
        this.memoryGameRepository = springContext.getBean(MemoryGameRepository.class);
        this.questionRepository = springContext.getBean(QuestionRepository.class);
    }

    /**
     * מצרף שחקן למשחק קיים או יוצר GameState חדש אם זה השחקן השני.
     * שחקן ראשון: יתווסף ל-gamePlayers בלבד.
     * שחקן שני: יווצר GameState חדש, המשחק מוכן.
     */
    @Override
    public synchronized void joinGame(Long sessionId, String userEmail) throws RemoteException {
        gamePlayers.putIfAbsent(sessionId, new HashSet<>());
        Set<String> players = gamePlayers.get(sessionId);

        if (players.size() < 2 && !players.contains(userEmail)) {
            players.add(userEmail);
            System.out.println("Player " + userEmail + " joined session " + sessionId);

            if (players.size() == 2) {
                Iterator<String> it = players.iterator();
                String player1 = it.next();
                String player2 = it.next();
                gameStates.put(sessionId, new GameState(sessionId, player1, player2));
                System.out.println("Game state initialized for session " + sessionId);
            }
        } else {
            System.out.println("Player already joined or game full");
        }
    }
    
    /**
     * יוצרת שאלות רק אם זה לא נוצר כבר עבור sessionId, ושומרת אותן במפה.
     * כל קריאה נוספת תחזיר את אותה רשימת שאלות.
     */
    @Override
    public synchronized List<Question> generateQuestionsForMemorySession(Long sessionId, String category) throws RemoteException {
        // if (!sessionQuestions.containsKey(sessionId)) {
            List<Question> questions = memoryGame.generateQuestionsForMemorySession(sessionId, category);
            sessionQuestions.put(sessionId, questions);
            return questions;
        // } else {
            // return sessionQuestions.get(sessionId);
        // }
    }

    @Override
    public synchronized MemoryGameSessionStartResponseDto retrieveQuestionsForMemorySession(Long sessionId) throws RemoteException
    {
         //question list
            List<MemoryQuestionDto> memoryQuestionDtos = sessionQuestions.get(sessionId).stream()
                .map(p -> new MemoryQuestionDto(
                    p.getQuestionText(),
                    true,
                    memoryGameRepository.findQuestionCardPositionByGameSesstionAndQuestion(sessionId ,p.getQuestionId()),
                    p.getImageUrl()))
                .collect(Collectors.toList());

            //answer list
            List<MemoryQuestionDto> memoryAnswerDtos =  sessionQuestions.get(sessionId).stream()
                .map(p -> new MemoryQuestionDto(
                    questionRepository.findCorrectAnswerByQuestion(p.getQuestionId()),
                    false,
                    memoryGameRepository.findAnswerCardPositionByGameSesstionAndQuestion(sessionId,p.getQuestionId()),
                     p.getImageUrl()))
                .collect(Collectors.toList());

                //Combine question and answer lists
                memoryQuestionDtos.addAll(memoryAnswerDtos);
                return new MemoryGameSessionStartResponseDto(sessionId, memoryQuestionDtos);
    }

    /**
     * בודק אם המשחק מוכן (יש שני שחקנים ו-GameState מאותחל)
     */
    public synchronized boolean isGameReady(Long sessionId) throws RemoteException {
        Set<String> players = gamePlayers.get(sessionId);
        return players != null && players.size() == 2 && gameStates.containsKey(sessionId);
    }

    @Override
    public synchronized boolean checkAnswer(Long sessionId, String userEmail, String currentPlayer, int selectedQuestionCard, int selectedAnswerCard) throws RemoteException {
        GameState gameState = gameStates.get(sessionId);

        if (gameState == null) {
            System.out.println("Game state not initialized for session " + sessionId);
            throw new RemoteException("Game state not initialized for session " + sessionId);
        }

        boolean isCorrect = this.memoryGame.checkAnswer(sessionId, userEmail, currentPlayer, selectedQuestionCard, selectedAnswerCard);
        if(userEmail.equals(currentPlayer))
        {
            if (isCorrect) {
                gameState.addPoint(userEmail);
            } else {
                System.out.println("switch turn");
                gameState.switchTurn();
            }
        }
        if(memoryGameRepository.areAllPairsFlipped(sessionId))
        {
            gameState.setGameOver(true);
        }
        return isCorrect;
    }

    /**
     * Returns a map with game state for polling: { ready: boolean, players: List<String>, currentPlayer: String, questions: List<Question> }
     */
    @Override
    public synchronized boolean getGameStatus(Long sessionId) throws RemoteException {
        Set<String> players = gamePlayers.get(sessionId);
        boolean ready = players != null && players.size() == 2 && gameStates.containsKey(sessionId);
        System.out.println("Game state for session " + sessionId + ": " + ready);
        return ready;
    }

    @Override
    public synchronized MemoryGameResultResponseDto getGameResultMemory(Long sessionId) throws RemoteException {
        GameState gameState = gameStates.get(sessionId);
        if (gameState == null) {
            throw new RemoteException("Game state not found for session " + sessionId);
        }

        return memoryGame.getGameResultMemory(sessionId, gameState.getScores());
    }

    @Override
    public synchronized void flipQuestion(Long sessionId, int questionIndex) throws RemoteException {
        System.out.println("In flip question: " + questionIndex);
        GameState gameState = gameStates.get(sessionId);
        if(gameState != null){
            gameState.setCurrentQuestion(questionIndex);
        }
    }

    @Override
    public synchronized void flipAnswer(Long sessionId, int answerIndex) throws RemoteException {
        System.out.println("In flip answer: " + answerIndex);
        GameState gameState = gameStates.get(sessionId);
        if(gameState != null){
            System.out.println("I was actually here");
            gameState.setCurrentAnswer(answerIndex);
        }
    }

    @Override
    public synchronized MemoryStateDto getGameState(Long sessionId) throws RemoteException
    {
        GameState gameState = gameStates.get(sessionId);
        if (gameState == null) {
            throw new RemoteException("Game state not found for session " + sessionId);
        }
    
        return new MemoryStateDto(gameState.getSessionId(), gameState.getCurrentQuestion(), gameState.getCurrentAnswer(), gameState.getCurrentPlayer(), gameState.isGameOver());
    }
}
