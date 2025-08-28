package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponseDto {
    private String answer;//The answer of bot- from back to front
    private String conversationId;//The conversation ID- always return new or existing- from back to front
}
