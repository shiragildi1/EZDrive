package com.ezdrive.ezdrive.api.dto;
import lombok.AllArgsConstructor;   
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAnswerRequestDto {
    private Long sessionId;
    private String currentPlayer;
    private int selectedQuestionCard; 
    private int selectedAnswerCard; 
}

