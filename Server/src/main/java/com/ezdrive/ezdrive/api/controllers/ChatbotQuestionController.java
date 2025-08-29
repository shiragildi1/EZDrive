package com.ezdrive.ezdrive.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.AgentQuestionPerConvDto;
import com.ezdrive.ezdrive.api.dto.AgentRequestDto;
import com.ezdrive.ezdrive.api.dto.AgentResponseDto;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.services.ChatbotQuestionService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/agent")
public class ChatbotQuestionController {
	@Autowired
	private ChatbotQuestionService chatbotQuestionService;

	// receive an answer for the question
	@PostMapping("/ask")
    public ResponseEntity<AgentResponseDto> askAgent(@RequestBody AgentRequestDto req,  HttpSession session) {
        String question = req.getQuestion();
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AgentResponseDto("couldnt send question", null));
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new AgentResponseDto("No user in session. connecto to receive an answer", null));
        }
        String conversationId = req.getConversationId();
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = java.util.UUID.randomUUID().toString(); //new conversations
        }
        String answer = chatbotQuestionService.answer(question);
        chatbotQuestionService.createBotQuestion(user, question, answer, conversationId);


        return ResponseEntity.ok(new AgentResponseDto(answer, conversationId));
    }

    //returns old conversations
    @GetMapping("/conversations")
public ResponseEntity<List<AgentQuestionPerConvDto>> getConversation(
    @RequestParam("conversationId") String conversationId, HttpSession session) {

    User user = (User) session.getAttribute("user");
    if (user == null) return ResponseEntity.status(401).build();

    List<AgentQuestionPerConvDto> history =
        chatbotQuestionService.getConversationHistory(user.getEmail(), conversationId);

    return ResponseEntity.ok(history); 
}

}

