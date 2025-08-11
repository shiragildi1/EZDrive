
// GameSessionStartRequestDto.java
package com.ezdrive.ezdrive.api.dto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSessionStartRequestDto {
    private String userEmail;
    private String gameType;
    private String category;
}

