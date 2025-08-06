package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResultDto {
    private int correctAnswerIndex;
    private boolean isCorrect;
}
