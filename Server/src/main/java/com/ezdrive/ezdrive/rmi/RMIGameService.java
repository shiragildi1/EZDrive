package com.ezdrive.ezdrive.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.ezdrive.ezdrive.api.dto.MemoryGameResultResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryGameSessionStartResponseDto;
import com.ezdrive.ezdrive.api.dto.MemoryStateDto;
import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface RMIGameService extends Remote{
    List<Question> generateQuestionsForMemorySession(Long sessionId, String category) throws RemoteException;
    boolean checkAnswer(Long sessionId, String userEmail, String currentPlayer, int selectedQuestionCard, int selectedAnswerCard) throws RemoteException;
    MemoryGameResultResponseDto getGameResultMemory(Long sessionId) throws RemoteException;
    void joinGame(Long sessionId, String userEmail) throws RemoteException;
    /**
     * Returns a map with game state for polling: { ready: boolean, players: List<String>, currentPlayer: String }
     */
   boolean getGameStatus(Long sessionId) throws RemoteException;
   MemoryStateDto getGameState(Long sessionId) throws RemoteException;
   void flipQuestion(Long sessionId, int questionIndex) throws RemoteException;
   void flipAnswer(Long sessionId, int answerIndex) throws RemoteException;
   MemoryGameSessionStartResponseDto retrieveQuestionsForMemorySession(Long sessionId) throws RemoteException;

}