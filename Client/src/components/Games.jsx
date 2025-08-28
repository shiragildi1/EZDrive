import React, { useState } from "react";
import { data, useSearchParams } from "react-router-dom";
import { startTriviaSession } from "../services/TriviaGameService";
import TriviaGame from "./TriviaGame";
import {
  startMemorySession,
  getOpponentA,
  getOpponentB,
} from "../services/MemoryGameServiceRMI";
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
} from "../services/MemoryGameServiceRMI";
import { useUserContext } from "../context/UserContext";

export default function GamesPage() {
  const { user } = useUserContext();
  const [searchParams] = useSearchParams();
  const topic = searchParams.get("topic");

  const [questions, setQuestions] = useState([]);
  const [opponent, setOpponent] = useState([]);
  const [sessionId, setSessionId] = useState(null);
  const [showTrivia, setShowTrivia] = useState(false);
  const [showMemory, setShowMemory] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loadingMemory, setLoadingMemory] = useState(false);
  const [selectedGame, setSelectedGame] = useState(null);
  const [showMemoryRoadmap, setShowMemoryRoadmap] = useState(false);
  const [showJoinInput, setShowJoinInput] = useState(false);
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
            imageUrl: question.imageUrl || null,
          };
        });

        setQuestions(formattedQuestions); // שמור את השאלות בפורמט החדש
        setSessionId(data.session.id); // שמור את מזהה הסשן
        //          setSessionId(data.sessionId); // שמור את מזהה הסשן
        // >>>>>>> origin/08/19-1-P
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
            const opponent = await getOpponentB(data.sessionId);
            console.log("opponent join: ", opponent);
            setQuestions(formattedQuestions); // שומר את השאלות המעובדות ב-state
            setOpponent(opponent);
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
            const formattedQuestions = data.questions.map((question) => ({
              cardId: question.cardId,
              isQuestion: question.question,
              text: question.text,
              imageUrl: question.imageURl || null,
            }));
            const opponent = await getOpponentA(data.sessionId);
            console.log("opponent join: ", opponent);
            setQuestions(formattedQuestions); // שומר את השאלות המעובדות ב-state
            setOpponent(opponent);
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
  if (showMemory) {
    return (
      <MemoryGame
        questions={questions}
        sessionId={sessionId}
        opponent={opponent}
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
  if (showMemoryRoadmap) {
    return (
      <div className="memory-roadmap-container">
        <h2 className="memory-roadmap-title">{topicsMap[topic]}</h2>

        <button className="gradient-button" onClick={handleStartMemory}>
          התחל משחק חדש
        </button>

        <button
          className="gradient-button"
          onClick={() => setShowJoinInput(true)}
        >
          הצטרף למשחק קיים
        </button>

        {showJoinInput && (
          <div className="join-section">
            <input
              type="text"
              placeholder="הכנס מזהה משחק"
              value={joinSessionId}
              onChange={(e) => setJoinSessionId(e.target.value)}
              className="session-input"
              disabled={joining}
            />
            <button
              className="gradient-button"
              onClick={handleJoinMemory}
              disabled={joining || !joinSessionId}
            >
              אשר הצטרפות
            </button>
          </div>
        )}

        <button
          className="back-button"
          onClick={() => setShowMemoryRoadmap(false)}
        >
          חזור
        </button>
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
            setShowMemoryRoadmap(true);
          }}
        >
          <h3>זכרון</h3>
        </button>
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
