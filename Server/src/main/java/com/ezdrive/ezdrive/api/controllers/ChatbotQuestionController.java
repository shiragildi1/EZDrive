package com.ezdrive.ezdrive.api.controllers;

import java.rmi.RemoteException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.persistence.Entities.ChatbotQuestions;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.ChatbotQuestionsRepository;
import com.ezdrive.ezdrive.services.ChatbotQuestionService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/agent")
public class ChatbotQuestionController {
	@Autowired
	private ChatbotQuestionService chatbotQuestionService;

	// קבלת תשובה לשאלה ע"י טקסט (POST)
	@PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askAgent(@RequestBody Map<String, String> body,  HttpSession session) {
        String question = body.get("question");
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("answer", "נא לשלוח טקסט של שאלה."));
        }
        String answer = chatbotQuestionService.answer(question.trim());

        User user = (User) session.getAttribute("user");
        chatbotQuestionService.createBotQuestion(user, question.trim(), answer);


        return ResponseEntity.ok(Map.of("answer", answer));
    }

}

