package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenRequestDto 
{
    @Getter
    @Setter
    private String token;

    public void setToken(String token) 
    {
        this.token = token;
    }
    
}
