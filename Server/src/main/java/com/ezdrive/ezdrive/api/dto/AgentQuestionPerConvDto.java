package com.ezdrive.ezdrive.api.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentQuestionPerConvDto {
    private Long id;
    private String question;
    private String answer;
}
