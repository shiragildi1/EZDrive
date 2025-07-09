import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getQuestionsByCategory } from "../services/GameCategoryService";
import TriviaGame from "./TriviaGame";
import GameExplanation from "./GameExplanation";
import HeadToHeadExplanation from "../data/HeadToHeadExplanationData";
import simulationExplanation from "../data/SimulationExplanationData";
import triviaExplanation from "../data/TriviaExplanationData";
import "../styles/Games.css"; // Assuming you have a CSS file for styling

export default function GamesPage() {
  const [searchParams] = useSearchParams();
  const topic = searchParams.get("topic");
  const [questions, setQuestions] = useState([]);
  const [showTrivia, setShowTrivia] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const [loading, setLoading] = useState(false);
  const [selectedGame, setSelectedGame] = useState(null);

  const topicsMap = {
    traffic: "חוקי התנועה",
    signs: "תמרורים",
    safety: "בטיחות",
    vehicle: "הכרת הרכב",
  };

  const handleStartTrivia = () => {
    setLoading(true);
    getQuestionsByCategory(topicsMap[topic])
      .then((data) => {
        setQuestions(
          data.map((q) => ({
            question: q.questionText,
            options: [q.answer1, q.answer2, q.answer3, q.answer4],
            correctAnswer: [q.answer1, q.answer2, q.answer3, q.answer4][
              q.correctAnswer - 1
            ],
          }))
        );
        setShowTrivia(true);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  };

  //hendeleStartHeadToHead

  //hendelStartSimulazya

  const getExplanationData = () => {
    switch (selectedGame) {
      case "trivia":
        return triviaExplanation;
      case "headToHead":
        return HeadToHeadExplanation;
      case "simulation":
        return simulationExplanation;
      default:
        console.error("Unknown game selected");
    }
  };

  const getStartHandler = () => {
    switch (selectedGame) {
      case "trivia":
        return handleStartTrivia;
      case "headToHead":
        return null;
      //!!!!!!!replace with actual head-to-head start logic
      case "simulation":
        return null;
      //!!!!!!!replace with actual simulation start logic
      default:
        return () => {};
    }
  };

  if (loading) return <div>טוען שאלות...</div>;

  if (showTrivia) {
    return <TriviaGame questions={questions} topic={topicsMap[topic]} />;
  }

  return (
    <div className="games-background">
      <h2>בחרת ב{topicsMap[topic]}</h2>
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
