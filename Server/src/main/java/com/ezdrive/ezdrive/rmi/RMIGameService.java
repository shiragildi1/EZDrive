package com.ezdrive.ezdrive.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface RMIGameService extends Remote{
    List<Question> generateQuestionsForMemorySession(Long sessionId, String category) throws RemoteException;
    public boolean checkAnswer(Long sessionId, String userEmail, int selectedQuestionCard, int selectedAnswerCard) throws RemoteException;
    
    }