package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitAnswerRequestDto {
    private Long sessionId;
    private Long questionId;
    private int selectedAnswer; // הערך שנבחר (1–4)
}

