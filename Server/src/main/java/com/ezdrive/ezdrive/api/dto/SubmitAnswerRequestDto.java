package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;
//from client to server
@Getter
@Setter
public class SubmitAnswerRequestDto {
    private Long sessionId;
    private Long questionId;
    private int selectedAnswer; 
}

