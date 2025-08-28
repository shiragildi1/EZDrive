 package com.ezdrive.ezdrive.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ezdrive.ezdrive.api.dto.AgentQuestionPerConv;
import com.ezdrive.ezdrive.persistence.Entities.ChatbotQuestions;
import com.ezdrive.ezdrive.persistence.Entities.User;
import com.ezdrive.ezdrive.persistence.Repositories.ChatbotQuestionsRepository;

@Service
public class ChatbotQuestionService {
    @Autowired
    private ChatbotQuestionsRepository chatbotQuestionsRepository;
    @Autowired


    private static final String GEMINI_API_KEY = "AIzaSyArbxIckbdnLr7ig92GGseyfKl8S5i7c-U";
    private static final String MODEL = "gemini-2.0-flash";
    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent?key=" + GEMINI_API_KEY;

    private final RestTemplate http = new RestTemplate();

    public String answer(String userQuestion) {
        if (userQuestion == null || userQuestion.isBlank()) {
            return "נא לנסח שאלה.";
        }

        try {
            // ---------- headers ----------
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // ---------- systemInstruction ----------
            String systemText = """
                    אתה עוזר לענייני תיאוריה לנהיגה בישראל (מבחן עיוני).
                    כללי עבודה:
                    1) ענה רק על שאלות תיאוריה בתחום נהיגה/תמרורים/תנועה/בטיחות על הכביש בישראל.
                    2) אסור להמציא מידע. אם אין לך ביטחון גבוה בתשובה — אמור במפורש: "מחוץ לתחום או לא בטוח".
                    3) השב תמיד בעברית, קצר, ברור, 2–4 משפטים.
                    4) אין לצטט חוקים/סעיפים אם אינך בטוח; אל תוסיף ידע שמעבר לנפוץ/מוסכם לתיאוריה לנהיגה.
                    5) אם השאלה לא קשורה לתיאוריה לנהיגה — אמור: "אני רשאי לענות רק על שאלות תיאוריה בנושא נהיגה ותנועה."
                    """;

            JSONObject systemInstruction = new JSONObject()
                    .put("role", "system")
                    .put("parts", new JSONArray().put(new JSONObject().put("text", systemText)));

            // ---------- generationConfig ----------
            JSONObject genCfg = new JSONObject()
                    .put("temperature", 0.2)
                    .put("response_mime_type", "text/plain");

            // ---------- user content ----------
            JSONObject userContent = new JSONObject()
                    .put("role", "user")
                    .put("parts", new JSONArray().put(new JSONObject().put("text", userQuestion)));

            // ---------- request body ----------
            JSONObject body = new JSONObject()
                    .put("systemInstruction", systemInstruction)
                    .put("generationConfig", genCfg)
                    .put("contents", new JSONArray().put(userContent));

            // ---------- call ----------
            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
            ResponseEntity<String> resp = http.postForEntity(URL, entity, String.class);

            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return "אירעה שגיאה מול שירות ה-AI. נסי שוב.";
            }

            JSONObject respBody = new JSONObject(resp.getBody());
            JSONArray candidates = respBody.optJSONArray("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "לא בטוח איך לענות על זה כרגע.";
            }

            JSONObject content0 = candidates.getJSONObject(0).optJSONObject("content");
            if (content0 == null) return "לא בטוח איך לענות על זה כרגע.";
            JSONArray partsArr = content0.optJSONArray("parts");
            if (partsArr == null || partsArr.isEmpty()) return "לא בטוח איך לענות על זה כרגע.";

            String text = partsArr.getJSONObject(0).optString("text", "").trim();
            if (text.isBlank()) return "לא בטוח איך לענות על זה כרגע.";
            return text;

        } catch (Exception e) {
            System.out.println("Gemini error: " + e.getMessage());
            return "אירעה שגיאה בעיבוד השאלה. נסי שוב.";
        }
    }

    public ChatbotQuestions createBotQuestion(User user, String question, String answer, String conversationId) {
        ChatbotQuestions chatbotQuestions = new ChatbotQuestions();
        chatbotQuestions.setUser(user);
        chatbotQuestions.setQuestion(question);
        chatbotQuestions.setAnswer(answer);
        chatbotQuestions.setConversationId(conversationId);

        // Save the game session to the database
        chatbotQuestionsRepository.save(chatbotQuestions);

        return chatbotQuestions;
    }

    public List<AgentQuestionPerConv> getConversationHistory(String email, String cid) {
    var rows = chatbotQuestionsRepository
                 .findByUser_EmailAndConversationIdOrderByIdAsc(email, cid);
    return rows.stream()
        .map(r -> new AgentQuestionPerConv(r.getId(), r.getQuestion(), r.getAnswer()))
        .toList();
}
}
