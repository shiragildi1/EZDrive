package com.ezdrive.ezdrive.api.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoryGameSessionStartResponseDto implements Serializable {
    private Long sessionId;
    private List<MemoryQuestionDto> questions;
}