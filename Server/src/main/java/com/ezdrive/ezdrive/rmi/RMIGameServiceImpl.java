package com.ezdrive.ezdrive.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.springframework.context.ApplicationContext;

import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.services.MemoryGameService;
import java.util.*;

public class RMIGameServiceImpl extends UnicastRemoteObject implements RMIGameService {
    private MemoryGameService memoryGame;

    private final List<String> players = new ArrayList<String>();
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
        System.out.println("HI RMI");
        return memoryGame.checkAnswer(sessionId, userEmail, selectedQuestionCard, selectedAnswerCard);
    }

    @Override
    public synchronized void joinGame(String userEmail) throws RemoteException
    {
        if(players.size() < 2 && !(players.contains(userEmail)))
        {
            players.add(userEmail);
            System.out.println("The player: " + userEmail + " joined");
        }
        else
        {
            System.out.println("player already joined");
        }
    }
}