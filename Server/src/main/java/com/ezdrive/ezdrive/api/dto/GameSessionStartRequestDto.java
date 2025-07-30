
// GameSessionStartRequestDto.java
package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSessionStartRequestDto {
    private String userEmail;
    private String gameType;
    private String category;
}

