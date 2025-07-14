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
@Table(name = "game_question_answers")
public class GameQuestionAnswer {

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

    // מספר התשובה שהמשתמש בחר (1–4), או null אם לא ענה
    @Column(name = "selected_answer")
    private Integer selectedAnswer;

    // האם התשובה נכונה
    @Column(name = "is_correct")
    private boolean isCorrect;

    // מתי ענה על השאלה (או פג הזמן)
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}


