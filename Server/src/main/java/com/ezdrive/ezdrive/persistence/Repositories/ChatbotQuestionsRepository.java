package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezdrive.ezdrive.persistence.Entities.ChatbotQuestions;


public interface ChatbotQuestionsRepository extends JpaRepository<ChatbotQuestions, Long> {
    List<ChatbotQuestions> findByUser_EmailAndConversationIdOrderByIdAsc(String email, String conversationId);
}
//return ChatbotQuestions{id=1, user=alice, conversationId="convA",
//                    question="מה זה תמרור עצור?", answer="תמרור עצור מחייב עצירה."},
//   ChatbotQuestions{id=2, user=alice, conversationId="convA",
//                    question="ומה אם אין רכב?", answer="עדיין חובה לעצור."},

//   ChatbotQuestions{id=3, user=alice, conversationId="convB",
//                    question="איך פונים שמאלה?", answer="יש לתת זכות קדימה."},
                   