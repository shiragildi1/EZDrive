import React, { useState } from "react";
import { data, useSearchParams } from "react-router-dom";
import { startTriviaSession } from "../services/GameTriviaService";
import TriviaGame from "./TriviaGame";
import { startMemorySession } from "../services/GameMemoryServiceRMI";
import MemoryGame from "./MemoryGame";
import GameExplanation from "./GameExplanation";
import HeadToHeadExplanation from "../data/HeadToHeadExplanationData";
import simulationExplanation from "../data/SimulationExplanationData";
import triviaExplanation from "../data/TriviaExplanationData";
import "../styles/Games.css";
import memoryExplanation from "../data/MemoryExplanationData";
import {
  joinMemoryGame,
  getMemoryGameStatus,
} from "../services/GameMemoryServiceRMI";
import { useUserContext } from "../context/UserContext";

export default function GamesPage() {
  const { user } = useUserContext();
  const [searchParams] = useSearchParams();
  const topic = searchParams.get("topic");

  const [questions, setQuestions] = useState([]);
  const [sessionId, setSessionId] = useState(null);
  const [showTrivia, setShowTrivia] = useState(false);
  const [showMemory, setShowMemory] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loadingMemory, setLoadingMemory] = useState(false);
  const [selectedGame, setSelectedGame] = useState(null);
  // memory game join flow
  const [joinSessionId, setJoinSessionId] = useState("");
  const [joining, setJoining] = useState(false);
  const [waitingForOpponent, setWaitingForOpponent] = useState(false);

  const topicsMap = {
    traffic: "חוקי התנועה",
    signs: "תמרורים",
    safety: "בטיחות",
    vehicle: "הכרת הרכב",
  };

  const handleStartTrivia = () => {
    setLoading(true); //start to load questions
    const category = topicsMap[topic];

    startTriviaSession({ category })
      .then((data) => {
        console.log("response from startTriviaSession:", data);

        // הפוך כל שאלה לאובייקט עם שדה options שמכיל את כל ארבע התשובות
        const formattedQuestions = data.questions.map((question) => {
          return {
            questionId: question.questionId,
            questionText: question.questionText,
            options: [
              question.answer1,
              question.answer2,
              question.answer3,
              question.answer4,
            ],
          };
        });

        setQuestions(formattedQuestions); // שמור את השאלות בפורמט החדש
        setSessionId(data.sessionId); // שמור את מזהה הסשן
        setShowTrivia(true);
        console.log("formattedQuestions:", formattedQuestions); // עבור למצב משחק
      })
      .catch((err) => {
        console.error("שגיאה ב-startTriviaSession:", err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const getExplanationData = () => {
    switch (selectedGame) {
      case "trivia":
        return triviaExplanation;
      case "memory":
        return memoryExplanation;
      case "headToHead":
        return HeadToHeadExplanation;
      case "simulation":
        return simulationExplanation;
      default:
        console.error("Unknown game selected");
        return {};
    }
  };

  const getStartHandler = () => {
    switch (selectedGame) {
      case "trivia":
        return handleStartTrivia;
      case "memory":
        return handleStartMemory;
      case "headToHead":
      case "simulation":
        return null;
      default:
        return () => {};
    }
  };

  if (loading) return <div>טוען שאלות...</div>;

  if (showTrivia) {
    return (
      <TriviaGame
        questions={questions}
        sessionId={sessionId}
        topic={topicsMap[topic]}
      />
    );
  }
  //------------------------------------------------------------------------------
  // --- זיכרון מול שחקן אחר ---

  // שחקן ראשון - יצירת משחק חדש
  const handleStartMemory = async () => {
    setLoadingMemory(true);
    setWaitingForOpponent(true);
    const category = topicsMap[topic];
    try {
      // שולח בקשה ל-backend ליצור משחק חדש ומקבל מזהה סשן
      // const sessionId = await startMemorySession(category);
      // setSessionId(sessionId);
      // מתחיל polling לבדוק אם המשחק מוכן (כלומר, אם שחקן שני הצטרף)
      const data = await startMemorySession(category);
      if (data == null) {
        throw new Error("No session returned form startMemory session");
      }
      setSessionId(data.sessionId);
      const interval = setInterval(async () => {
        try {
          // מקבל את מצב המשחק והשאלות מה-backend
          const ready = await getMemoryGameStatus(data.sessionId);
          if (ready) {
            clearInterval(interval);
            // ממפה את השאלות לפורמט אחיד עבור הקומפוננטה
            // כל שאלה הופכת לאובייקט עם cardId, isQuestion, text
            const formattedQuestions = data.questions.map((question) => ({
              cardId: question.cardId,
              isQuestion: question.question,
              text: question.text,
              imageUrl: question.imageURl || null,
            }));
            console.log("FORMATTED: ", formattedQuestions);
            setQuestions(formattedQuestions); // שומר את השאלות המעובדות ב-state
            setShowMemory(true); // עובר למצב משחק זיכרון
            setSessionId(data.sessionId); // שומר את מזהה הסשן
            setWaitingForOpponent(false); // מפסיק להציג את מסך ההמתנה
          }
        } catch (err) {
          console.error("[Memory] שגיאה ב-polling:", err);
        }
      }, 2000);
    } catch (err) {
      console.error("[Memory] שגיאה ביצירת משחק:", err);
      setWaitingForOpponent(false);
    } finally {
      setLoadingMemory(false);
    }
  };

  // שחקן שני - מצטרף למשחק קיים
  const handleJoinMemory = async () => {
    if (!joinSessionId) return;
    setJoining(true);
    setWaitingForOpponent(true);
    try {
      // שולח בקשה ל-backend להצטרף למשחק קיים
      const data = await joinMemoryGame(joinSessionId);
      setSessionId(joinSessionId);
      // מתחיל polling לבדוק אם המשחק מוכן (כלומר, אם שני שחקנים מחוברים)
      const interval = setInterval(async () => {
        try {
          // מקבל את מצב המשחק והשאלות מה-backend
          const ready = await getMemoryGameStatus(joinSessionId);
          if (ready) {
            clearInterval(interval);
            // ממפה את השאלות לפורמט אחיד עבור הקומפוננטה
            console.log("IMage url back0: ", data.questions[9].imageURl);
            const formattedQuestions = data.questions.map((question) => ({
              cardId: question.cardId,
              isQuestion: question.question,
              text: question.text,
              imageUrl: question.imageURl || null,
            }));
            setQuestions(formattedQuestions); // שומר את השאלות המעובדות ב-state
            setShowMemory(true); // עובר למצב משחק זיכרון

            setWaitingForOpponent(false); // מפסיק להציג את מסך ההמתנה
          }
        } catch (err) {
          console.error("[Memory] שגיאה ב-polling:", err);
        }
      }, 2000);
    } catch (err) {
      console.error("[Memory] שגיאה בהצטרפות למשחק:", err);
      setWaitingForOpponent(false);
    } finally {
      setJoining(false);
    }
  };
  if (showMemory) {
    return (
      <MemoryGame
        questions={questions}
        sessionId={sessionId}
        topic={topicsMap[topic]}
      />
    );
  }
  // מחכה ליריב
  if (waitingForOpponent) {
    return (
      <div className="waiting-modal">
        <div className="waiting-for-opponent">מחפש לך יריב...</div>
        {sessionId && (
          <div className="waiting-session-id">
            מזהה משחק שלך:{" "}
            <span style={{ fontWeight: "bold" }}>{sessionId}</span>
            <br />
            שלח את המספר הזה לחבר כדי שיצטרף!
          </div>
        )}
      </div>
    );
  }
  //-------------------------------------------------------------------------------
  return (
    <div className="games-background">
      <h2>{topicsMap[topic]}</h2>
      <div className="game-buttons">
        <button
          className="square-game"
          onClick={() => {
            setSelectedGame("trivia");
            setShowPopup(true);
          }}
        >
          <h3>טריוויה</h3>
        </button>

        <button
          className="square-game"
          onClick={() => {
            setSelectedGame("memory");
            setShowPopup(true);
          }}
        >
          <h3>זכרון</h3>
        </button>
        {/* UI for joining memory game as second player */}
        <div style={{ marginTop: 10 }}>
          <input
            type="text"
            placeholder="הכנס מזהה משחק להצטרפות (sessionId)"
            value={joinSessionId}
            onChange={(e) => setJoinSessionId(e.target.value)}
            style={{ direction: "ltr", width: 200 }}
            disabled={joining}
          />
          <button
            onClick={handleJoinMemory}
            disabled={joining || !joinSessionId}
            style={{ marginRight: 8 }}
          >
            הצטרף למשחק קיים
          </button>
        </div>
        <button
          className="square-game"
          onClick={() => {
            setSelectedGame("headToHead");
            setShowPopup(true);
          }}
        >
          <h3>ראש בראש</h3>
        </button>
        <button
          className="square-game"
          onClick={() => {
            setSelectedGame("simulation");
            setShowPopup(true);
          }}
        >
          <h3>סימולציה</h3>
        </button>
      </div>

      {showPopup && (
        <GameExplanation
          {...getExplanationData()}
          onStart={getStartHandler()}
          onClose={() => {
            setShowPopup(false);
            setSelectedGame(null);
          }}
        />
      )}
    </div>
  );
}
