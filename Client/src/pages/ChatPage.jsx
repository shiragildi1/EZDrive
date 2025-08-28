import React, { useState } from "react";
import "../styles/Chat.css";
import { sendQuestion } from "../services/ChatService";

export default function ChatPage() {
  const [question, setQuestion] = useState("");
  const [currentSession, setCurrentSession] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [activeSession, setActiveSession] = useState(null);
  const [loading, setLoading] = useState(false);

  // שליחת שאלה
  const handleSend = async () => {
    const q = question.trim();
    if (!q) return;

    setLoading(true);
    const newMessages = [...currentSession, { question: q, answer: "⌛ טוען תשובה..." }];
    setCurrentSession(newMessages);

    
      const answerObj = await sendQuestion(q);
      newMessages[newMessages.length - 1].answer = answerObj.answer;
      setCurrentSession([...newMessages]);

      if (activeSession !== null) {
        const updatedSessions = [...sessions];
        updatedSessions[activeSession].messages.push({ question: q, answer: answerObj.answer });
        setSessions(updatedSessions);
      }
      console.log("Received answer:", answerObj.answer);
      setQuestion("");
      setLoading(false);
  };

  // פתיחת שיחה חדשה
  const handleNewChat = () => {
    if (currentSession.length > 0) {
      const sessionId = "שיחה " + (sessions.length + 1);
      setSessions([...sessions, { title: sessionId, messages: [...currentSession] }]);
    }
    setCurrentSession([]);
    setActiveSession(null);
  };

  // מעבר לשיחה ישנה
  const handleSessionClick = (idx) => {
    setActiveSession(idx);
    setCurrentSession([...sessions[idx].messages]);
  };

  return (
    <div className="chat-page">
      <div className="sidebar-chat">
        <h2>היסטוריה</h2>
        <button id="newChatBtn" onClick={handleNewChat}>שיחה חדשה</button>
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
          <button id="sendBtn" onClick={handleSend} disabled={loading}>שלח</button>
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