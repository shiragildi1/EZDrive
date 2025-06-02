package com.ezdrive.ezdrive.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.GoogleTokenRequest;
import com.ezdrive.ezdrive.services.AuthService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
    @Autowired
    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenRequest request) 
    {
        try 
        {
            String email = authService.verifyToken(request.getToken());
            return ResponseEntity.ok(Collections.singletonMap("email", email)); 
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}