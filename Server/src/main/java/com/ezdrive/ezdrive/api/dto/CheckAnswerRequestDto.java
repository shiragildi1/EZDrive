package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckAnswerRequestDto {
    private Long sessionId;
    private String userEmail;
    private int selectedQuestionCard; 
    private int selectedAnswerCard; 
}

