package com.ezdrive.ezdrive.api.dto;

import lombok.Getter;
import lombok.Setter;
public class EmailRequestDto 
{
    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String code;

    // public String getEmail() 
    // { 
    //     return email; 
    // }
    // public void setEmail(String email) 
    // {
    //     this.email = email;
    // }
    // public String getCode() 
    // { 
    //     return code; 
    // }
    // public void setCode(String code) 
    // {
    //     this.code = code;
    // }   
}
