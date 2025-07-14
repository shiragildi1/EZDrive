package com.ezdrive.ezdrive.api.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.persistence.Entities.User;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    @GetMapping("/me")
    public User getCurrentUser(HttpSession session) 
    {
        System.out.println("Session ID:" + session.getId());
        System.out.println("Session User:" + session.getAttribute("user"));
        return (User) session.getAttribute("user");
    }
}

