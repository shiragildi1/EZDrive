package com.ezdrive.ezdrive.rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class GameState {
    private String currentPlayer;
    private Map<String, Integer> scores = new HashMap<>();
    private Long sessionId;


    public GameState(Long sessionId, String player1, String player2) {
        this.sessionId = sessionId;
        this.currentPlayer = player1;
        scores.put(player1, 0);
        scores.put(player2, 0);
    }

    // public String getCurrentPlayer() {
    //     return currentPlayer;
    // }

    public void switchTurn() {
        List<String> players = new ArrayList<>(scores.keySet());
        if (players.size() == 2) {
            currentPlayer = currentPlayer.equals(players.get(0)) ? players.get(1) : players.get(0);
            System.out.println("[GameState] Turn switched. Current player: " + currentPlayer);
        }
    }

    public void addPoint(String playerEmail) {
        int newScore = scores.getOrDefault(playerEmail, 0) + 1;
        scores.put(playerEmail, newScore);
        System.out.println("[GameState] Player " + playerEmail + " scored a point. New score: " + newScore);
    }

    public int getScore(String playerEmail) {
        return scores.getOrDefault(playerEmail, 0);
    }
}
