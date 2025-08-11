package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemoryGameResultResponseDto {
    private int totalQuestions;             
    private int correctAnswersPlayer1;      
    private int correctAnswersPlayer2;      
    private String totalTime;               
    private int score;                      
}


