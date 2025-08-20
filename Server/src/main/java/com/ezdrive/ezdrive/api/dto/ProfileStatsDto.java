package com.ezdrive.ezdrive.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileStatsDto {
    private GameStatDto memory;
    private GameStatDto trivia;
    private GameStatDto simulation;
}

