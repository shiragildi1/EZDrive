import "../styles/YourTriviaAnswer.css";
import React, { useState, useEffect } from "react";
/// This component displays the user's trivia answers in a popup.
/// It is designed to be used within a modal or popup context.
import { getGameSummary } from "../services/GameTriviaService";

export default function YourTriviaAnswer({ sessionId, onClose }) {
  const [answers, setAnswers] = useState([]);

  useEffect(() => {
    const fetchAnswers = async () => {
      try {
        const data = await getGameSummary(sessionId);
        console.log("data from server:", data);

        if (Array.isArray(data)) {
          setAnswers(data);
        } else {
          console.error("Expected an array, got:", data);
          setAnswers([]);
        }
      } catch (error) {
        console.error("Failed to fetch answers:", error);
        setAnswers([]);
      }
    };

    fetchAnswers();
  }, [sessionId]);

  return (
    <div className="popup-backdrop-TriviaAnswer">
      <div className="popup-box-TriviaAnswer">
        <button className="close-button-TriviaAnswer" onClick={onClose}>
          X
        </button>
        <h2>התשובות שלך</h2>

        <div className="answers-grid">
          {answers.map((item, index) => (
            <div
              key={index}
              className={`answer-box ${item.correct ? "correct" : "wrong"}`}
            >
              <p className="question-text">{item.question}</p>
              <p>
                <strong>ענית:</strong> {item.userAnswer}
              </p>
              {!item.correct && (
                <p>
                  <strong>תשובה נכונה:</strong> {item.correctAnswer}
                </p>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
