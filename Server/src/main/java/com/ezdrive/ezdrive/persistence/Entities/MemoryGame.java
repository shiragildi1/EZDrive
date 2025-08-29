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

    @ManyToOne
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    // (Question)
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // holds question card position
    @Column(name = "question_card")
    private Integer questionCard;

    // holds answer card position
    @Column(name = "answer_card")
    private Integer answerCard;

    // was the pair found
    @Column(name = "is_flipped")
    private boolean isFlipped;

    // who flipped the pair
    @Column(name = "player_answered")
    private String playerAnsweredEmail;

    
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}


