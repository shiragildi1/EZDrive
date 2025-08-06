package com.ezdrive.ezdrive.api.controllers;

import java.rmi.RemoteException;
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
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;
import com.ezdrive.ezdrive.services.GameSessionService;
import com.ezdrive.ezdrive.services.TriviaGameService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/game-sessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private TriviaGameService triviaGameService;

    @Autowired
    private RMIGameServiceImpl memoryGameService;

    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
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
    public MemoryGameSessionStartResponseDto startMemorySession(
        @RequestBody MemoryGameStartRequestDto request,
        HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        String userEmail = user.getEmail();
        String userEmail2 = user.getEmail();

        GameSession gameSession = gameSessionService.createMemoryGameSession(userEmail, userEmail2, request.getGameType(), request.getCategory());

        List<Question> pairs = null;
        try {
            pairs = memoryGameService.generateQuestionsForMemorySession(gameSession.getId(), request.getCategory());
        } catch(RemoteException e) {
            System.out.println("Error");
        }

        //question list
        List<MemoryQuestionDto> memoryQuestionDtos = pairs.stream()
            .map(p -> new MemoryQuestionDto(
                p.getQuestionText(),
                true,
                memoryGameRepository.findQuestionCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId())))
            .collect(Collectors.toList());

        //answer list
        List<MemoryQuestionDto> memoryAnswerDtos = pairs.stream()
            .map(p -> new MemoryQuestionDto(
                questionRepository.findCorrectAnswerByQuestion(p.getQuestionId()),
                false,
                memoryGameRepository.findAnswerCardPositionByGameSesstionAndQuestion(gameSession.getId(),p.getQuestionId())))
            .collect(Collectors.toList());

        //combine lists
        memoryQuestionDtos.addAll(memoryAnswerDtos);

        return new MemoryGameSessionStartResponseDto(gameSession, memoryQuestionDtos);
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
            isCorrect = memoryGameService.checkAnswer(
                request.getSessionId(),
                user.getEmail(), // ⬅️ מהסשן
                request.getSelectedQuestionCard(),
                request.getSelectedAnswerCard()
            );
        } catch (RemoteException e) {
            System.out.println("Error");
        }

        return ResponseEntity.ok(isCorrect);
    }
}
