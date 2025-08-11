package com.ezdrive.ezdrive.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.GoogleTokenRequestDto;
import com.ezdrive.ezdrive.exceptions.UserAlreadyExistsException;
import com.ezdrive.ezdrive.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.ezdrive.ezdrive.api.dto.EmailRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.User;

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
        HttpSession session = req.getSession(); // יוצר סשן חדש אם אין
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
    public ResponseEntity<?> emailLogin(@RequestBody EmailRequestDto request) 
    {
        try 
        {
            String message = authService.registerEmailUser(request.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } 
        catch (UserAlreadyExistsException e) 
        {
            if ("User already exists".equals(e.getMessage())) 
            {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }   
}       