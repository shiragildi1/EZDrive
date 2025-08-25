package com.ezdrive.ezdrive.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.services.AgentService;

@RestController
@RequestMapping("/agent")
public class AgentController {
	@Autowired
	private AgentService agentService;

	// קבלת תשובה לשאלה ע"י טקסט (POST)
	@PostMapping("/answer")
	public ResponseEntity<String> getAnswer(@RequestBody String body) {
        if (body == null || body.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("נא לשלוח טקסט של שאלה.");
        }
        String question = body.trim();
        if ((question.startsWith("\"") && question.endsWith("\"")) ||
            (question.startsWith("'") && question.endsWith("'"))) {
            question = question.substring(1, question.length()-1);
        }
        String answer = agentService.answer(question);
        return ResponseEntity.ok(answer);
    }
}
