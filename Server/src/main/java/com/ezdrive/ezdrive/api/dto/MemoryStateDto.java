package com.ezdrive.ezdrive.api.dto;
import java.io.Serializable;

import lombok.AllArgsConstructor;   
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoryStateDto implements Serializable{
    private Long sessionId;
    private int flippedQuestion; 
    private int flippedAnswer; 
    private String currentPlayer;
    private boolean gameOver;
}
