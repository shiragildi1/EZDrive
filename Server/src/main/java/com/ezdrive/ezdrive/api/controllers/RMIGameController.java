package com.ezdrive.ezdrive.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rmi-game")
public class RMIGameController {

    @GetMapping("/status")
    public String getStatus() {
        return "RMI Game Controller is