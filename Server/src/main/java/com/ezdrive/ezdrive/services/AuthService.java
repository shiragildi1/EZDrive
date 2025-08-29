package com.ezdrive.ezdrive.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.persistence.Entities.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

// Service for handling authentication
@Service
public class AuthService 
{

    @Value("${google.client.id}")
    private String clientId;

    @Autowired
    private UserService userService;

    // Verifies the Google ID token
    private GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception
    {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) 
        {
            return idToken.getPayload();
        } 
        else 
        {
            throw new RuntimeException("Invalid ID token.");
        }
    }


    // Registers a Google user
    public User registerGoogleUser(String idTokenString) throws Exception
    {
        GoogleIdToken.Payload payload = verifyToken(idTokenString);
        String email = payload.getEmail();

        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) 
        {
            System.out.println("User already exists, logging in...");
            return existingUser.get();
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName((String) payload.get("name"));
        newUser.setPicture((String) payload.get("picture"));
        newUser.setGoogleId(payload.getSubject());
        newUser.setEmailVerified(Boolean.TRUE.equals(payload.getEmailVerified()));
        newUser.setGivenName((String) payload.get("given_name"));
        newUser.setFamilyName((String) payload.get("family_name"));

        userService.createUser(newUser);
        System.out.println("New user created successfully");
        return newUser;
    }

    // Registers an email user
    public String registerEmailUser(String email)
    {
        if (userService.findByEmail(email).isPresent()) 
        {
            return "User already exists, logging in...";
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(" ");
        newUser.setPicture(" ");
        newUser.setGoogleId(" ");
        newUser.setEmailVerified(true);
        newUser.setGivenName(" ");
        newUser.setFamilyName(" ");

        userService.createUser(newUser);
        return "New user created successfully";
    }

    
}