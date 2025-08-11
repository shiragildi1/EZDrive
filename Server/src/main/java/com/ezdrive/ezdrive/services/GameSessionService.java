package com.ezdrive.ezdrive.services;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.GameSessionRepository;
import com.ezdrive.ezdrive.persistence.Repositories.UserRepository;

@Service
public class GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;
    
    @Autowired
    private UserRepository userRepository;

    public GameSession createGameSession(String userEmail, String gameType, String category) {
        User user = userRepository.findByEmail(userEmail)
    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));


        GameSession gameSession = new GameSession();
        gameSession.setUser(user);
        gameSession.setGameType(gameType);
        gameSession.setCategory(category);
        gameSession.setPlayedAt(LocalDateTime.now());
        gameSession.setScore(0); // Initialize score to 0, can be updated later during the game

        // Save the game session to the database
        gameSessionRepository.save(gameSession);

        return gameSession;
    }

     public GameSession createMemoryGameSession(String userEmail, String userEmail2, String gameType, String category) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        GameSession gameSession = new GameSession();
        gameSession.setUser(user);
        // אפשר ליצור session בלי user2 (שחקן שני)
        if (userEmail2 != null) {
            User user2 = userRepository.findByEmail(userEmail2)
                .orElseThrow(() -> new IllegalArgumentException("User2 not found with email: " + userEmail2));
            gameSession.setUser2(user2);
        } else {
            gameSession.setUser2(null);
        }
        gameSession.setGameType(gameType);
        gameSession.setCategory(category);
        gameSession.setPlayedAt(LocalDateTime.now());
        gameSession.setScore(0); // Initialize score to 0, can be updated later during the game

        // Save the game session to the database
        gameSessionRepository.save(gameSession);

        return gameSession;
    }
}