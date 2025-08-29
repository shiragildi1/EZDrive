package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezdrive.ezdrive.persistence.Entities.ChatbotQuestions;


public interface ChatbotQuestionsRepository extends JpaRepository<ChatbotQuestions, Long> {
    List<ChatbotQuestions> findByUser_EmailAndConversationIdOrderByIdAsc(String email, String conversationId);
}


