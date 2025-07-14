package com.ezdrive.ezdrive.persistence.Repositories;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    
}
