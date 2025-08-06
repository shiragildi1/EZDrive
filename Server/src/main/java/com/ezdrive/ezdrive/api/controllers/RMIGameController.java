package com.ezdrive.ezdrive.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.rmi.RMIGameService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RestController
@RequestMapping("/api/rmi-game")
@CrossOrigin(origins = "*")

public class RMIGameController {
    private RMIGameService rmiGameService;

    @PostMapping("/join")
    public ResponseEntity<Boolean> joinGame(@RequestBody String userEmail) {
        try {
            rmiGameService.joinGame(userEmail);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(false);
        }
    }
}