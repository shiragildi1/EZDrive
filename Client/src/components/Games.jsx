import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import { startTriviaSession } from "../services/GameTriviaService";
import TriviaGame from "./TriviaGame";
import { startMemorySession } from "../services/GameMemoryService";
import MemoryGame from "./MemoryGame";
import GameExplanation from "./GameExplanation";
import HeadToHeadExplanation from "../data/HeadToHeadExplanationData";
import simulationExplanation from "../data/SimulationExplanationData";
import triviaExplanation from "../data/TriviaExplanationData";
import "../styles/Games.css";
import memoryExplanation from "../data/MemoryExplanationData";

export default function GamesPage() {
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

  const topicsMap = {
    traffic: "חוקי התנועה",
    signs: "תמרורים",
    safety: "בטיחות",
    vehicle: "הכרת הרכב",
  };

  const handleStartTrivia = () => {
    setLoading(true); //start to load questions
    const userEmail = "pessyisraeli@gmail.com";
    const category = topicsMap[topic];

    startTriviaSession(userEmail, category)
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
        setSessionId(data.session.id); // שמור את מזהה הסשן
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
        return memoryExplanation; ////change
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
  const handleStartMemory = () => {
    setLoadingMemory(true); //start to load questions
    const userEmail = "pessyisraeli@gmail.com";
    const category = topicsMap[topic];

    startMemorySession(userEmail, category)
      .then((data) => {
        console.log("response from startMemorySession:", data);
        const formattedQuestions = data.questions.map((question) => {
          return {
            cardPosition: question.cardPosition,
            question: question.question,
            text: question.text,
          };
        });

        setQuestions(formattedQuestions); // שמור בפורמט החדש
        setSessionId(data.session.id); // שמור את מזהה הסשן
        setShowMemory(true);
      })

      .catch((err) => {
        console.error("שגיאה ב-startMemorySession:", err);
      })
      .finally(() => {
        setLoading(false);
      });
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
