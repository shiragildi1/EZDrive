package com.ezdrive.ezdrive.api.controllers;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.GameStatDto;
import com.ezdrive.ezdrive.api.dto.ProfileStatsDto;
import com.ezdrive.ezdrive.services.ProfileService;

@RestController
@RequestMapping("/api/profile")

public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/stats")
    public ProfileStatsDto getProfileStats(@RequestParam String userEmail, @RequestParam String range) {
        Map<String, Long> counts = profileService.getGameCounts(userEmail, range);
        Map<String, Long> avgs   = profileService.getGameAverage(userEmail, range);
        GameStatDto memory = new GameStatDto(
                counts.getOrDefault("memory", 0L),
                avgs.getOrDefault("memory", 0L)
        );
        GameStatDto trivia = new GameStatDto(
                counts.getOrDefault("trivia", 0L),
                avgs.getOrDefault("trivia", 0L)
        );

        GameStatDto simulation = new GameStatDto(
                counts.getOrDefault("simulation", 0L),
                avgs.getOrDefault("simulation", 0L)
        );

        return new ProfileStatsDto(memory, trivia, simulation);
    }
}
