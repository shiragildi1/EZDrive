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

import com.ezdrive.ezdrive.api.dto.CheckAnswerRequestDto;
import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryQuestionDto;
import com.ezdrive.ezdrive.api.dto.QuestionTriviaDto;
import com.ezdrive.ezdrive.api.dto.SubmitAnswerRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Repositories.MemoryGameRepository;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;
import com.ezdrive.ezdrive.services.GameSessionService;
import com.ezdrive.ezdrive.services.TriviaGameService;



@RestController
@RequestMapping("/game-sessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private TriviaGameService triviaGameService;

    @Autowired
    private RMIGameServiceImpl memoryGameService;
   // private MemoryGameService memoryGameService;

    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private QuestionRepository questionRepository;


    @PostMapping("/start")
    public GameSessionStartResponseDto startSession(
        @RequestParam String userEmail,
        @RequestParam String gameType,
        @RequestParam String category) {

        GameSession session = gameSessionService.createGameSession(userEmail, gameType, category);

        List<Question> questions = triviaGameService.generateQuestionsForSession(session.getId(), category);

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

        return new GameSessionStartResponseDto(session, questionDtos);
    }

    @PostMapping("/submit-answer")
    public ResponseEntity<Void> submitAnswer(@RequestBody SubmitAnswerRequestDto request) {
        triviaGameService.submitAnswer(
            request.getSessionId(),
            request.getQuestionId(),
            request.getSelectedAnswer());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/result")
    public GameResultResponseDto getGameResult(@RequestParam Long sessionId) {
        return triviaGameService.getGameResult(sessionId);
    }
    //----------------------------------------memory---------------------------------

    @PostMapping("/startMemory")
    public MemoryGameSessionStartResponseDto startMemorySession(
        @RequestParam String userEmail,
        @RequestParam String userEmail2,
        @RequestParam String gameType,
        @RequestParam String category) {

        GameSession session = gameSessionService.createMemoryGameSession(userEmail, userEmail2, gameType, category);

        List<Question> pairs =null ;
        try{
            pairs = memoryGameService.generateQuestionsForMemorySession(session.getId(), category);
        }
        catch(RemoteException e)
        {
            System.out.println("Error");
        }
           
        //question list
        List<MemoryQuestionDto> memoryQuestionDtos = pairs.stream()
            .map(p -> new MemoryQuestionDto(
                p.getQuestionText(),
                true,
                memoryGameRepository.findQuestionCardPositionByGameSesstionAndQuestion(session.getId(),p.getQuestionId())))
            .collect(Collectors.toList());

        //answer list   
        List<MemoryQuestionDto> memoryAnswerDtos = pairs.stream()
            .map(p -> new MemoryQuestionDto(
                questionRepository.findCorrectAnswerByQuestion(p.getQuestionId()),
                false,
                memoryGameRepository.findAnswerCardPositionByGameSesstionAndQuestion(session.getId(),p.getQuestionId())))
            .collect(Collectors.toList());

            //combine lists
            memoryQuestionDtos.addAll(memoryAnswerDtos);

        return new MemoryGameSessionStartResponseDto(session, memoryQuestionDtos);
    }

    //Memory game
    @PostMapping("/check-answer")
    public ResponseEntity<Boolean> checkAnswer(@RequestBody CheckAnswerRequestDto request) {
        boolean isCorrect = false;
        try{ isCorrect = memoryGameService.checkAnswer(
            request.getSessionId(),
            request.getUserEmail(),
            request.getSelectedQuestionCard(),
            request.getSelectedAnswerCard()
        );}
        catch(RemoteException e)
        {
            System.out.println("Error");
        }
        
        return ResponseEntity.ok(isCorrect);
    }

}
