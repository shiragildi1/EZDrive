package com.ezdrive.ezdrive.api.dto;

import lombok.NoArgsConstructor;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MemoryQuestionDto {
    private String text;
    private boolean question;
    private int cardId;
}