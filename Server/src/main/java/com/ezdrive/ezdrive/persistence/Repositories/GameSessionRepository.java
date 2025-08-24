package com.ezdrive.ezdrive.persistence.Repositories;

import com.ezdrive.ezdrive.persistence.Entities.GameSession;
import com.ezdrive.ezdrive.persistence.Entities.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    @Query("SELECT gs.user FROM GameSession gs WHERE gs.id = :sessionId")
    User getUserBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT gs.user2 FROM GameSession gs WHERE gs.id = :sessionId")
    User getUser2BySessionId(@Param("sessionId") Long sessionId);
}
