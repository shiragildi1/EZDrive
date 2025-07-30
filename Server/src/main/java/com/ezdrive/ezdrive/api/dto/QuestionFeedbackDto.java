package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionFeedbackDto {
    private String question;
    private String userAnswer;
    private String correctAnswer;
    private boolean isCorrect;
}
