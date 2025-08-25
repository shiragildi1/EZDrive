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
public class MemoryQuestionDto implements Serializable {
    private String text;
    private boolean question;
    private int cardId;
    private String imageURl;
}