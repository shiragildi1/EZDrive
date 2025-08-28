package com.ezdrive.ezdrive.api.dto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentQuestionPerConv {
    private Long id;
    private String question;
    private String answer;
}
