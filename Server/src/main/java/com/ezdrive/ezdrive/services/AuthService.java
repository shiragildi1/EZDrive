package com.ezdrive.ezdrive.services;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class AuthService 
{

    // @Value("${google.client.id}")
    private String clientId="250713696752-4k026jggdvnitarn3efqudooqignqc68.apps.googleusercontent.com";

    public String verifyToken(String idTokenString) throws Exception 
    {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) 
        {
            GoogleIdToken.Payload payload = idToken.getPayload();
            return payload.getEmail(); 
        }
        else 
        {
            throw new RuntimeException("Invalid ID token.");
        }
    }
}