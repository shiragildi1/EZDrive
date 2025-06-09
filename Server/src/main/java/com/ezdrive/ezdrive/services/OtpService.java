package com.ezdrive.ezdrive.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.ezdrive.ezdrive.model.OtpData;

import org.springframework.mail.javamail.JavaMailSender;




@Service
public class OtpService 
{
    private Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    @Autowired
    private JavaMailSender mailSender;

    public String createOtpData(String email)
    {
        String code= generateOtpCode();
        System.out.println("the otp coode is: " + code);
        OtpData otp = new OtpData(code, System.currentTimeMillis() + 10 * 60 * 1000, 0);
        otpStore.put(email, otp);
        sendEmail(email, code);
        return code;
    }
    
    private String generateOtpCode()
    {
        int code = (int)(Math.random() * 900_000) + 100_000;
        return String.valueOf(code);
    }

    private void sendEmail(String to, String code) {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your code for EZDrive application");
        message.setText("your code is: " + code);
        mailSender.send(message);
    } 
    catch (Exception e) 
    {
        e.printStackTrace(); 
    }
}

    public boolean verifyOtp(String email, String code)
    {
        OtpData otpData= otpStore.get(email);
        if(otpData == null)
        {
            return false;
        }
        if (System.currentTimeMillis() > otpData.getExpiry()) 
        {
            otpStore.remove(email);
            System.out.println("the code expiryTime");
            return false;
        }
        if (otpData.getCode().equals(code)) 
        {
        otpStore.remove(email); 
        return true;
        } 
        else 
        {
            otpData.setAttempts(otpData.getAttempts() + 1); 
            return false;
        }
    }
}
