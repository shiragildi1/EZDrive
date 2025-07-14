package com.ezdrive.ezdrive.api.controllers;

import com.ezdrive.ezdrive.api.dto.GameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.GameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.QuestionDto;
import com.ezdrive.ezdrive.api.dto.SubmitAnswerRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.services.GameQuestionAnswerService;
import com.ezdrive.ezdrive.services.GameSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game-sessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameQuestionAnswerService gameQuestionAnswerService;

    @PostMapping("/start")
    public GameSessionStartResponseDto startSession(
        @RequestParam String userEmail,
        @RequestParam String gameType,
        @RequestParam String category) {

        GameSession session = gameSessionService.createGameSession(userEmail, gameType, category);

        List<Question> questions = gameQuestionAnswerService.generateQuestionsForSession(session.getId(), category);

        List<QuestionDto> questionDtos = questions.stream()
            .map(q -> new QuestionDto(
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
        gameQuestionAnswerService.submitAnswer(
            request.getSessionId(),
            request.getQuestionId(),
            request.getSelectedAnswer());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/result")
    public GameResultResponseDto getGameResult(@RequestParam Long sessionId) {
        return gameQuestionAnswerService.getGameResult(sessionId);
    }
}
