package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoryGameStartRequestDto {
    private String gameType;
    private String category;
}
