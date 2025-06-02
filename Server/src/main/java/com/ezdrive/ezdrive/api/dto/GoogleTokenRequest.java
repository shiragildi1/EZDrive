package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;

public class GoogleTokenRequest 
{
    @Getter
    @Setter
    private String token;

    public void setToken(String token) 
    {
        this.token = token;
    }
    
}
