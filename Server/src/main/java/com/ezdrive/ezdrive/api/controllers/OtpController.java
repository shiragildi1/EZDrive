
package com.ezdrive.ezdrive.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.EmailRequestDto;
import com.ezdrive.ezdrive.services.OtpService;
import com.ezdrive.ezdrive.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/otp")
public class OtpController
{
    @Autowired
    private OtpService otpService;

    @PostMapping("/create")
    public ResponseEntity<?> createOtp(@RequestBody EmailRequestDto request) 
    {
        try 
        {
            String code = otpService.createOtpData(request.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("otp", code));
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Failed to generate OTP"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody EmailRequestDto request, HttpServletRequest req) {
        try {
            boolean isValid = otpService.verifyOtp(request.getEmail(), request.getCode());
            return ResponseEntity.ok(Collections.singletonMap("valid", isValid));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Collections.singletonMap("error", "Failed"));
        }
    }

}