package com.ezdrive.ezdrive.persistence.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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

    // קשר למשתמש ששיחק
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    // קשר למשתמש ששיחק
    @ManyToOne
    @JoinColumn(name = "user_email2")
    private User user2;


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
