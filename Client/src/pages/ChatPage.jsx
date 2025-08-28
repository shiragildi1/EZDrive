import React, { useState } from "react";
import "../styles/Chat.css";
import { sendQuestion } from "../services/ChatService";

export default function ChatPage() {
  const [question, setQuestion] = useState("");
  const [currentSession, setCurrentSession] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [activeSession, setActiveSession] = useState(null);
  const [loading, setLoading] = useState(false);
  const [currentConversationId, setCurrentConversationId] = useState(null);

  // שליחת שאלה
  const handleSend = async () => {
    const q = question.trim();
    if (!q) return;

    setLoading(true);

    // טיוטה עדכנית
    const draft = [
      ...currentSession,
      { question: q, answer: "⌛ טוען תשובה..." },
    ];
    setCurrentSession(draft);

    try {
      // שולחים את ה-cid אם יש; אם אין — הבאק ייצור חדש ויחזיר
      const { answer, conversationId } = await sendQuestion(
        q,
        currentConversationId
      );

      setCurrentConversationId(conversationId);

      // מחליפים את הודעת הטעינה בתשובה האמיתית
      draft[draft.length - 1].answer = answer;
      setCurrentSession([...draft]);

      if (activeSession !== null) {
        // מעדכן שיחה קיימת בסיידבר
        const updated = [...sessions];
        updated[activeSession] = {
          ...updated[activeSession],
          id: conversationId,
          messages: [...draft],
        };
        setSessions(updated);
      } else {
        // שאלה ראשונה בשיחה חדשה → פותחים פריט חדש בסיידבר
        const title = "שיחה " + (sessions.length + 1);
        const newSessions = [
          ...sessions,
          { id: conversationId, title, messages: [...draft] },
        ];
        setSessions(newSessions);
        setActiveSession(newSessions.length - 1);
      }
    } catch (e) {
      const failed = [...draft];
      failed[failed.length - 1].answer = "שגיאה בשליחה. נסי שוב.";
      setCurrentSession(failed);
      console.error(e);
    } finally {
      setQuestion("");
      setLoading(false);
    }
  };

  // פתיחת שיחה חדשה: שומר (אם צריך) את הנוכחית ומאפס מזהה שיחה
  const handleNewChat = () => {
    if (currentSession.length > 0 && activeSession === null) {
      // שומר רק אם לא בשיחה קיימת
      const title = "שיחה " + (sessions.length + 1);
      setSessions([
        ...sessions,
        { id: currentConversationId, title, messages: [...currentSession] },
      ]);
    }
    setCurrentSession([]);
    setActiveSession(null);
    setCurrentConversationId(null);
  };

  // מעבר לשיחה ישנה
  const handleSessionClick = (idx) => {
    setActiveSession(idx);
    setCurrentSession([...sessions[idx].messages]);
    setCurrentConversationId(sessions[idx].id);
  };

  return (
    <div className="chat-page">
      <div className="sidebar-chat">
        <h2>היסטוריה</h2>
        <button id="newChatBtn" onClick={handleNewChat}>
          שיחה חדשה
        </button>
        <ul id="historyList">
          {sessions.map((s, idx) => (
            <li key={idx} onClick={() => handleSessionClick(idx)}>
              {s.title}
            </li>
          ))}
        </ul>
      </div>
      <div className="container-chat">
        <div id="chatDisplay" className="chat-display">
          {currentSession.map((msg, idx) => (
            <React.Fragment key={idx}>
              <div className="chat-question">{msg.question}</div>
              <div className="chat-answer">{msg.answer}</div>
            </React.Fragment>
          ))}
        </div>
        <div className="input-container">
          <button id="sendBtn" onClick={handleSend} disabled={loading}>
            שלח
          </button>
          <input
            id="questionInput"
            type="text"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            disabled={loading}
            placeholder="הקלד שאלה..."
          />
        </div>
        <div className="privacy-text">
          המידע בצ'אט נשמר רק לצורך שיפור השירות ואינו מועבר לצד שלישי.
        </div>
      </div>
    </div>
  );
}