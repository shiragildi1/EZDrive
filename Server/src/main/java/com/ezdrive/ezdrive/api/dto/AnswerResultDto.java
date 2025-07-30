package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResultDto {
    private int correctAnswerIndex;
    private boolean isCorrect;
}
