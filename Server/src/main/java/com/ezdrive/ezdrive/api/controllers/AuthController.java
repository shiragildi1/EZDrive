package com.ezdrive.ezdrive.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.EmailRequestDto;
import com.ezdrive.ezdrive.api.dto.GoogleTokenRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
    @Autowired
    private AuthService authService;


    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenRequestDto request, HttpServletRequest req) {
        HttpSession session = req.getSession(); 
        try {
            User user = authService.registerGoogleUser(request.getToken());
            session.setAttribute("user", user); 
            System.out.println("New session ID: " + session.getId());
            System.out.println("User logged in: " + user.getEmail());
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @PostMapping("/email")
    public ResponseEntity<?> emailLogin(@RequestBody EmailRequestDto request, HttpServletRequest req) {
    try {
      
        User user = authService.registerEmailUser(request.getEmail());

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User logged in"));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Collections.singletonMap("error", e.getMessage()));
    }
    }  
}       