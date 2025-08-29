package com.ezdrive.ezdrive.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpData {
    private String code;//otp
    private long expiry;//expiration time
    private int attempts;//number of tries

    
    public OtpData(String code, long expiry, int attempts)
    {
        this.code = code;
        this .expiry = expiry;
        this.attempts = attempts;
    }
}
