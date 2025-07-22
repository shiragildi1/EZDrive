package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionTriviaDto {
    private Long questionId;
    private String questionText;
    private String category;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
}