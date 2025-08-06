package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultResponseDto {
    private int score;
    private int numberOfCorrectAnswers;
    private String totalTimeSession;
}

