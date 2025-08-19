package com.ezdrive.ezdrive.api.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemoryGameResultResponseDto implements Serializable {
    private Map<String, Integer> scores;
}