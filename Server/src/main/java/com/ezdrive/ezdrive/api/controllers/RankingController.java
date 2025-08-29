package com.ezdrive.ezdrive.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.api.dto.RankingStatsDto;
import com.ezdrive.ezdrive.services.RankingService;

//controls the ranking
@RestController 
@RequestMapping("/api/ranking")
public class RankingController {
    @Autowired
    private RankingService rankingService;

    //returns the ranking of all the users
    @GetMapping("/stats")
    public List<RankingStatsDto> getAllUsersTotalScores() {
        return rankingService.getAllUsersTotalScores();
    }
}
