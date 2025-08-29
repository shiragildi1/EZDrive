
package com.ezdrive.ezdrive.api.controllers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.AnswerResultDto;
import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartRequestDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryCheckAnswerRequestDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameStartRequestDto;
import com.ezdrive.ezdrive.api.dto.MemoryQuestionDto;
import com.ezdrive.ezdrive.api.dto.MemoryStateDto;
import com.ezdrive.ezdrive.api.dto.QuestionFeedbackDto;
import com.ezdrive.ezdrive.api.dto.QuestionTriviaDto;
import com.ezdrive.ezdrive.api.dto.TriviaSubmitAnswerRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.GameSessionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.MemoryGameRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.rmi.RMIGameService;
import com.ezdrive.ezdrive.services.GameSessionService;
import com.ezdrive.ezdrive.services.TriviaGameService;

import jakarta.servlet.http.HttpSession;

//Controls all the endpoints of all games and game sessions

@RestController
@RequestMapping("/game-sessions")
public class GameSessionController {
    private RMIGameService memoryGameService;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameSessionRepository gameSessionRepository;

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
    @PostMapping("/start-trivia")
    public GameSessionStartResponseDto startSession(@RequestBody GameSessionStartRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not found in session");
        }
        String userEmail =  user.getEmail();
        String gameType = request.getGameType();
        String category = request.getCategory();

        GameSession gameSession = gameSessionService.createGameSession(userEmail, gameType, category);

        List<Question> questions = triviaGameService.generateQuestionsForTriviaSession(gameSession.getId(), category);

        List<QuestionTriviaDto> questionDtos = questions.stream()
        .map(q -> new QuestionTriviaDto(
                q.getQuestionId(),
                q.getQuestionText(),
                q.getCategory(),
                q.getAnswer1(),
                q.getAnswer2(),
                q.getAnswer3(),
                q.getAnswer4(),
                q.getImageUrl()))
            .collect(Collectors.toList());
        return new GameSessionStartResponseDto(gameSession, questionDtos);
    }

    //sumbits the players answer
    @PostMapping("/submit-answer")
    public ResponseEntity<AnswerResultDto> submitAnswer(@RequestBody TriviaSubmitAnswerRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        AnswerResultDto result =
        triviaGameService.submitAnswer(
            request.getSessionId(),
            request.getQuestionId(),
            request.getSelectedAnswer());
        System.out.println("submitAnswer result: " + result);
        return ResponseEntity.ok(result);
    }

    //returns the score 
    @GetMapping("/result-trivia")
    public GameResultResponseDto getTriviaGameResult(@RequestParam Long sessionId) {
        return triviaGameService.getTriviaGameResult(sessionId);    
    }

    //returns the feedback for the trivia
   @GetMapping("/summary")
    public ResponseEntity<List<QuestionFeedbackDto>> getFeedback(@RequestParam Long sessionId) {
        List<QuestionFeedbackDto> summary = triviaGameService.getSessionFeedback(sessionId);
                
        return ResponseEntity.ok(summary);
    }

    //----------------------------------------------memory-----------------------------------------------------

    //starts the memory game
    @PostMapping("/start-memory")
    public MemoryGameSessionStartResponseDto startMemorySession(@RequestBody MemoryGameStartRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        GameSession gameSession = gameSessionService.createMemoryGameSession(user.getEmail(), null, request.getGameType(), request.getCategory());
        try {
            getRmiService().joinGame(gameSession.getId(), user.getEmail());
            // Generate questions for the memory game session
            List<Question> questions = getRmiService().generateQuestionsForMemorySession(gameSession.getId(), request.getCategory());
                  
            //question list
            List<MemoryQuestionDto> memoryQuestionDtos = questions.stream()
                .map(p -> new MemoryQuestionDto(
                    p.getQuestionText(),
                    true,
                    memoryGameRepository.findQuestionCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId()),
                    p.getImageUrl()))
                .collect(Collectors.toList());

            //answer list
            List<MemoryQuestionDto> memoryAnswerDtos = questions.stream()
                .map(p -> new MemoryQuestionDto(
                    questionRepository.findCorrectAnswerByQuestion(p.getQuestionId()),
                    false,
                    memoryGameRepository.findAnswerCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId()),
                    p.getImageUrl()))
                .collect(Collectors.toList());

                //Combine question and answer lists
                memoryQuestionDtos.addAll(memoryAnswerDtos);
                return new MemoryGameSessionStartResponseDto(gameSession.getId(), memoryQuestionDtos);
        } catch (RemoteException e) {
            return new MemoryGameSessionStartResponseDto(null, null);
        }
    }

    //allows a second player to join the game
    @PostMapping("/join-memory")
    public MemoryGameSessionStartResponseDto joinMemoryGame(@RequestParam Long sessionId, HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new MemoryGameSessionStartResponseDto(null, null);
        }

        GameSession gameSession = gameSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));

        gameSession.setUser2(user);
        gameSessionRepository.save(gameSession);
        System.out.println("User 2 controller: "+ user.getEmail());
        System.out.println(gameSession.getUser2());
        try {
            getRmiService().joinGame(sessionId, user.getEmail());
            
            MemoryGameSessionStartResponseDto response = memoryGameService.retrieveQuestionsForMemorySession(sessionId);
            return response;
        } catch (RemoteException e) {
            System.err.println("Failed to join memory game: " + e.getMessage());
            return new MemoryGameSessionStartResponseDto(null, null);
        }
    }

    //returns the opponents info 
    @GetMapping("/get-opponent-b")
    public ResponseEntity<User> getPlayerB(@RequestParam Long sessionId) {
        User opponent = gameSessionRepository.getUser2BySessionId(sessionId);
        return ResponseEntity.ok(opponent);
    }

    //returns the opponents info 
    @GetMapping("/get-opponent-a")
    public ResponseEntity<User> getPlayerA(@RequestParam Long sessionId) {
        User opponent = gameSessionRepository.getUserBySessionId(sessionId);
        return ResponseEntity.ok(opponent);
    }

    //flip question on opponents board
    @PostMapping("/flip-question")
    public ResponseEntity<Void> flipQuestion(@RequestParam Long sessionId, @RequestParam int questionIndex) {
        try {
            System.out.println("Flipping question at index: " + questionIndex + " for session: " + sessionId);
            getRmiService().flipQuestion(sessionId, questionIndex);
            return ResponseEntity.ok().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(500).build();
        }
    }

    //flip answer on opponents board
    @PostMapping("/flip-answer")
    public ResponseEntity<Void> flipAnswer(@RequestParam Long sessionId, @RequestParam int answerIndex) {
        try {
            getRmiService().flipAnswer(sessionId, answerIndex);
            return ResponseEntity.ok().build();
        } catch (RemoteException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Endpoint for polling memory game status (for frontend polling)
    @GetMapping("/memory-status")
    public ResponseEntity<Boolean> getMemoryGameStatus(@RequestParam Long sessionId) {
        try {
            // The RMI service should provide a method to get the game state for polling
            // For example: getGameState(sessionId) returns a POJO or Map with readiness and player info
            boolean state = getRmiService().getGameStatus(sessionId);
            return ResponseEntity.ok(state);
        } catch (RemoteException e) {
            return ResponseEntity.ok(false);
        }
    }

    //returns the current game state, all the info to update opponents board
@GetMapping("/memory-state")
public MemoryStateDto getMemoryGameState(@RequestParam Long sessionId)
{
    try {
        MemoryStateDto state = getRmiService().getGameState(sessionId);
        return state;
    } catch (RemoteException e) {
        return new MemoryStateDto();
    }
}

    //checks whether flipped pair is correct
    @PostMapping("/check-answer")
    public ResponseEntity<Boolean> checkAnswer(@RequestBody MemoryCheckAnswerRequestDto request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        boolean isCorrect = false;
        try {
            isCorrect = getRmiService().checkAnswer(
                request.getSessionId(),
                user.getEmail(), 
                request.getCurrentPlayer(),
                request.getSelectedQuestionCard(),
                request.getSelectedAnswerCard()
            );
        } catch (RemoteException e) {
            System.out.println("Error");
        }

        return ResponseEntity.ok(isCorrect);
    }

    //returns the result of the memory game
    @GetMapping("/result-memory")
    public MemoryGameResultResponseDto getMemoryGameResult(@RequestParam Long sessionId) {
        try {
            return getRmiService().getMemoryGameResult(sessionId);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get memory game result: " + e.getMessage());
        }
    }

   //deletes the old entries of finished memory games
    @DeleteMapping("/delete-memory-entries")
    public ResponseEntity<Void> deleteMemoryEntries(@RequestParam Long sessionId) {
    try {
        getRmiService().deleteMemoryEntries(sessionId);
        return ResponseEntity.noContent().build(); // 204 No Content
    } catch (RemoteException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();// 500 Internal Server Error
    }
}
}