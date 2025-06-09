package com.ezdrive.ezdrive.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpData {
    private String code;//קוד
    private long expiry;//זמן תפוגה
    private int attempts;//מספר ניסיונות

    
    public OtpData(String code, long expiry, int attempts)
    {
        this.code = code;
        this .expiry = expiry;
        this.attempts = attempts;
    }
}
