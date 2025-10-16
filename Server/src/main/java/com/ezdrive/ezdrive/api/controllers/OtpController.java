
package com.ezdrive.ezdrive.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.EmailRequestDto;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.services.AuthService;
import com.ezdrive.ezdrive.services.OtpService;

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

    @Autowired
    private AuthService authService;

    //create an OTP
    @PostMapping("/create")
    public ResponseEntity<?> createOtp(@RequestBody EmailRequestDto request) 
    {
        try 
        {
            String code = otpService.createOtpData(request.getEmail());
            System.out.println("Generated OTP code: " + code);
            return ResponseEntity.ok(Collections.singletonMap("otp", code));
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Failed to generate OTP"));
        }
    }

    //Verifies the OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody EmailRequestDto request, HttpServletRequest req) {
    try {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getCode());
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("valid", false));
        }

        User user = authService.registerEmailUser(request.getEmail());

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        System.out.println("New session ID: " + session.getId());
        System.out.println("User logged in (OTP): " + user.getEmail());

        var auth = new UsernamePasswordAuthenticationToken(
            user, null, java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.ok(java.util.Map.of("valid", true));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Collections.singletonMap("error", "Failed"));
    }
}

}