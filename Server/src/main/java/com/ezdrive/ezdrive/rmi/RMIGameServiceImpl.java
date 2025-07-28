package com.ezdrive.ezdrive.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.services.MemoryGameService;

public class RMIGameServiceImpl extends UnicastRemoteObject implements RMIGameService {
    private MemoryGameService memoryGame;
    public RMIGameServiceImpl() throws RemoteException {
        super();
    }

    public RMIGameServiceImpl(ApplicationContext springContext) throws RemoteException {
        super();
        this.memoryGame = springContext.getBean(MemoryGameService.class);
    }

    @Override
    public List<Question> generateQuestionsForMemorySession(Long sessionId, String category) throws RemoteException {
        return memoryGame.generateQuestionsForMemorySession(sessionId, category);
    }

    @Override
    public boolean checkAnswer(Long sessionId, String userEmail, int selectedQuestionCard, int selectedAnswerCard) throws RemoteException {
        return memoryGame.checkAnswer(sessionId, userEmail, selectedQuestionCard, selectedAnswerCard);
    }
}