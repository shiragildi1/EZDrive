package com.ezdrive.ezdrive.persistence.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "game_sessions")
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // קשר למשתמש ששיחק
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    // סוג המשחק: "trivia", "simulation", "head_to_head"
    @Column(name = "game_type")
    private String gameType;

    // קטגוריה: "חוקי התנועה", "תמרורים" וכו'
    @Column(name = "category")
    private String category;

    // ניקוד סופי (0–100)
    @Column(name = "score")
    private Integer score;

    
    @Column(name = "played_at")
    private LocalDateTime playedAt;
}
