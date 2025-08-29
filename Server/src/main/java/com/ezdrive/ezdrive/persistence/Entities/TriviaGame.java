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
@Table(name = "Trivia_Game")
public class TriviaGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation to gameSession
    @ManyToOne
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    //relation to Question
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;


    @Column(name = "selected_answer")
    private Integer selectedAnswer;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}


