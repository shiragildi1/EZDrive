package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameResultResponseDto {
    private int totalQuestions;
    private int correctAnswers;
    private int score; // יכול להיות באחוזים או מספר נקי
}

