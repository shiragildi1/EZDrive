// package com.ezdrive.ezdrive.api.controllers;

// import java.rmi.Naming;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.ezdrive.ezdrive.api.dto.CheckAnswerRequestDto;
// import com.ezdrive.ezdrive.rmi.RMIGameService;

// import io.swagger.v3.oas.annotations.parameters.RequestBody;

// @RestController
// @RequestMapping("/api/memory")
// public class MemoryGameController {
//     @PostMapping("/check-answer")
//     public boolean checkAnswer(@RequestBody CheckAnswerRequestDto req) throws Exception {
//         RMIGameService game = (RMIGameService) Naming.lookup("rmi://localhost/GameService");
//         return game.checkAnswer(req.getSessionId(), req.getUserEmail(), req.getSelectedQuestionCard(), req.getSelectedAnswerCard());
//     }


    
// }
