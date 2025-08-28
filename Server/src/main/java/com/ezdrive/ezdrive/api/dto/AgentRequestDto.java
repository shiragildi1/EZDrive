package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentRequestDto {
    private String question;//The question that user send- from front to back
    private String conversationId;//optional- from front to back if continue a previous conversation
}