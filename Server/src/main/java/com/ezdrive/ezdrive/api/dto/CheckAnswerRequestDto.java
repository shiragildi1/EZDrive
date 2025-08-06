package com.ezdrive.ezdrive.api.dto;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;   
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAnswerRequestDto {
    private Long sessionId;
    private int selectedQuestionCard; 
    private int selectedAnswerCard; 
}

