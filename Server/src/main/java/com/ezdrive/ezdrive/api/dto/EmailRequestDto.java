package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDto 
{
    private String email;
    private String code; 
}
