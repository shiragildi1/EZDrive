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
@Table(name = "memory_game")
public class MemoryGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // קשר למשחק (GameSession)
    @ManyToOne
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    // קשר לשאלה (Question)
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // מספר כרטיס שמכיל את השאלה
    @Column(name = "question_card")
    private Integer questionCard;

    // מספר כרטיס שמכיל את התשובה
    @Column(name = "answer_card")
    private Integer answerCard;

    // האם הזוג נבחר
    @Column(name = "is_flipped")
    private boolean isFlipped;

    //מי הפך את הזוג
    @Column(name = "player_answered")
    private String playerAnsweredEmail;

    // מתי ענה על השאלה (או פג הזמן)
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}


