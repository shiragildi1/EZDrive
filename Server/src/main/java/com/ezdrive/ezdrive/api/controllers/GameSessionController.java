
package com.ezdrive.ezdrive.api.controllers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.AnswerResultDto;
import com.ezdrive.ezdrive.api.dto.CheckAnswerRequestDto;
import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartRequestDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameStartRequestDto;
import com.ezdrive.ezdrive.api.dto.MemoryQuestionDto;
import com.ezdrive.ezdrive.api.dto.QuestionFeedbackDto;
import com.ezdrive.ezdrive.api.dto.QuestionTriviaDto;
import com.ezdrive.ezdrive.api.dto.SubmitAnswerRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.MemoryGameRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.rmi.RMIGameService;
import com.ezdrive.ezdrive.services.GameSessionService;
import com.ezdrive.ezdrive.services.TriviaGameService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/game-sessions")
public class GameSessionController {
    private RMIGameService memoryGameService;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private TriviaGameService triviaGameService;

    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    private RMIGameService getRmiService() {
    if (memoryGameService == null) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            memoryGameService = (RMIGameService) registry.lookup("RMIGameService");
            System.out.println("Connected to RMI service");
        } catch (Exception e) {
            System.err.println("Failed to connect to RMI service: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("RMI service not connected");
        }
    }
    return memoryGameService;
}

    //--------------------------------------game session---------------------------------------------------
    //--------------------------------------trivia--------------------------------------------------------
    @PostMapping("/start")
    public GameSessionStartResponseDto startSession(@RequestBody GameSessionStartRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not found in session");
        }
        String userEmail =  user.getEmail();
        String gameType = request.getGameType();
        String category = request.getCategory();

        GameSession gameSession = gameSessionService.createGameSession(userEmail, gameType, category);

        List<Question> questions = triviaGameService.generateQuestionsForSession(gameSession.getId(), category);

        List<QuestionTriviaDto> questionDtos = questions.stream()
        .map(q -> new QuestionTriviaDto(
                q.getQuestionId(),
                q.getQuestionText(),
                q.getCategory(),
                q.getAnswer1(),
                q.getAnswer2(),
                q.getAnswer3(),
                q.getAnswer4()))
            .collect(Collectors.toList());
        return new GameSessionStartResponseDto(gameSession, questionDtos);
    }

    @PostMapping("/submit-answer")
    public ResponseEntity<AnswerResultDto> submitAnswer(@RequestBody SubmitAnswerRequestDto request) {
        AnswerResultDto result =
        triviaGameService.submitAnswer(
            request.getSessionId(),
            request.getQuestionId(),
            request.getSelectedAnswer());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/result")
    public GameResultResponseDto getGameResult(@RequestParam Long sessionId) {
        return triviaGameService.getGameResult(sessionId);    
    }

   @GetMapping("/summary")
    public ResponseEntity<List<QuestionFeedbackDto>> getFeedback(@RequestParam Long sessionId) {
        List<QuestionFeedbackDto> summary = triviaGameService.getSessionFeedback(sessionId);
        
        return ResponseEntity.ok(summary);
    }

    //----------------------------------------------memory-----------------------------------------------------

    @PostMapping("/startMemory")
    public MemoryGameSessionStartResponseDto startMemorySession(@RequestBody MemoryGameStartRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // שחקן ראשון יוצר סשן חדש
        GameSession gameSession = gameSessionService.createMemoryGameSession(user.getEmail(), null, request.getGameType(), request.getCategory());
        try {
            getRmiService().joinGame(gameSession.getId(), user.getEmail());
            // Generate questions for the memory game session
            List<Question> questions = getRmiService().generateQuestionsForMemorySession(gameSession.getId(), request.getCategory());
            System.out.println("Generated questions for memory game session: " + questions);
                  
            //question list
            List<MemoryQuestionDto> memoryQuestionDtos = questions.stream()
                .map(p -> new MemoryQuestionDto(
                    p.getQuestionText(),
                    true,
                    memoryGameRepository.findQuestionCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId())))
                .collect(Collectors.toList());

            //answer list
            List<MemoryQuestionDto> memoryAnswerDtos = questions.stream()
                .map(p -> new MemoryQuestionDto(
                    questionRepository.findCorrectAnswerByQuestion(p.getQuestionId()),
                    false,
                    memoryGameRepository.findAnswerCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId())))
                .collect(Collectors.toList());

                //Combine question and answer lists
                memoryQuestionDtos.addAll(memoryAnswerDtos);
            System.out.println("Memory question DTOs: " + memoryQuestionDtos);
                return new MemoryGameSessionStartResponseDto(gameSession.getId(), memoryQuestionDtos);
        } catch (RemoteException e) {
            return new MemoryGameSessionStartResponseDto(null, null);
        }
    }

    @PostMapping("/join-memory")
    public MemoryGameSessionStartResponseDto joinMemoryGame(@RequestParam Long sessionId, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
         System.out.println("handleJoinMemoryBack response:111");
       return new MemoryGameSessionStartResponseDto(null,null);
    }

    try {
        System.out.println("handleJoinMemoryBdjkajkjkk");
        getRmiService().joinGame(sessionId, user.getEmail());
        
        MemoryGameSessionStartResponseDto response = memoryGameService.retrieveQuestionsForMemorySession(sessionId);
        System.out.println("Before error");
        System.out.println("handleJoinMemoryBack responsegood:"+ response);
        return response;
    } catch (RemoteException e) {
         System.out.println("handleJoinMemoryBack response:222 "+e);
        return new MemoryGameSessionStartResponseDto(null,null);
    }
}

    // Endpoint for polling memory game state (for frontend polling)
    @GetMapping("/memory-state")
    public ResponseEntity<Boolean> getMemoryGameState(@RequestParam Long sessionId) {
        try {
            // The RMI service should provide a method to get the game state for polling
            // For example: getGameState(sessionId) returns a POJO or Map with readiness and player info
            boolean state = getRmiService().getGameState(sessionId);
            System.out.println("Memory game state  in controllerfor session " + sessionId + ": " + state);
            return ResponseEntity.ok(state);
        } catch (RemoteException e) {
            return ResponseEntity.ok(false);
        }
    }


    //Memory game
    @PostMapping("/check-answer")
    public ResponseEntity<Boolean> checkAnswer(@RequestBody CheckAnswerRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        boolean isCorrect = false;
        try {
            isCorrect = getRmiService().checkAnswer(
                request.getSessionId(),
                user.getEmail(), 
                request.getSelectedQuestionCard(),
                request.getSelectedAnswerCard()
            );
        } catch (RemoteException e) {
            System.out.println("Error");
        }

        return ResponseEntity.ok(isCorrect);
    }

    @GetMapping("/result-memory")
    public MemoryGameResultResponseDto getGameResultMemory(@RequestParam Long sessionId) {
        try {
            return getRmiService().getGameResultMemory(sessionId);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get memory game result: " + e.getMessage());
        }
    }
}
