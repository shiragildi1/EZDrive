package com.ezdrive.ezdrive.api.dto;

import java.util.List;
import com.ezdrive.ezdrive.persistence.Entities.GameSession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameSessionStartResponseDto {
    private GameSession session;
    private List<QuestionDto> questions;
}
