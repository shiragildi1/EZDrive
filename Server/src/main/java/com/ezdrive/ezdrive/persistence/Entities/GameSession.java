package com.ezdrive.ezdrive.persistence.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_email2")
    private User user2;


    // game type "trivia", "simulation", "Memory"
    @Column(name = "game_type")
    private String gameType;

    @Column(name = "category")
    private String category;

    // final score- percent
    @Column(name = "score")
    private Integer score;

    // final score for second player in memory game-percent
    @Column(name = "score2")
    private Integer score2;


    @Column(name = "played_at")
    private LocalDateTime playedAt;
}
