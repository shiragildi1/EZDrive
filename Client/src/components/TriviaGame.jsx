import React, { useState, useEffect } from "react";
import { submitAnswer, getGameResult } from "../services/GameTriviaService";
import "../styles/TriviaGame.css";

export default function TriviaGame({ questions, sessionId, topic }) {
  const [current, setCurrent] = useState(0);
  const [selected, setSelected] = useState(null);
  const [showResult, setShowResult] = useState(false);
  const [timer, setTimer] = useState(30);
  const [gameOver, setGameOver] = useState(false);
  const [scoreResult, setScoreResult] = useState(null);

  const question = questions[current];

  console.log("Current question:", question);

  if (!question || !Array.isArray(question.options)) {
    return <div>טוען שאלות- קצת סבלנות ומתחילים</div>;
  }

  useEffect(() => {
    if (gameOver) return;

    const intervalId = setInterval(() => {
      setTimer((prev) => {
        const next = prev - 1;
        if (next <= 0) {
          clearInterval(intervalId);
          handleAnswer(null);
          return 0;
        }
        return next;
      });
    }, 1000);

    return () => clearInterval(intervalId);
  }, [current, gameOver]);

  async function handleAnswer(optionText) {
    setSelected(optionText);
    setShowResult(true);

    const selectedAnswerIndex = question.options.indexOf(optionText) + 1;

    await submitAnswer({
      sessionId,
      questionId: question.questionId,
      selectedAnswer: optionText ? selectedAnswerIndex : null,
    });

    setTimeout(async () => {
      setShowResult(false);
      setSelected(null);

      if (current < questions.length - 1) {
        setCurrent((prev) => prev + 1);
        setTimer(30);
      } else {
        const result = await getGameResult(sessionId);
        setScoreResult(result);
        setGameOver(true);
      }
    }, 2000);
  }

  if (gameOver && scoreResult) {
    return (
      <div className="trivia-background">
        <div className="trivia-header-row">
          <div className="trivia-header-content">
            <div className="trivia-title">סיימת את המשחק!</div>
            <div className="trivia-subtitle">התוצאה שלך: {scoreResult.score} נקודות</div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="trivia-background">
      <div className="trivia-header-row">
        <div className="trivia-header-content">
          <div className="trivia-title">מוכנים לאתגר?</div>
          <div className="trivia-subtitle">טריוויה בנושא {topic}</div>
        </div>
        <span className="trivia-timer">{timer}</span>
      </div>

      <div className="trivia-progress">
        {current + 1} / {questions.length}
      </div>

      <div className="trivia-question-box">
        <div className="trivia-question">{question.questionText}</div>
      </div>

      <div className="trivia-options">
        {question.options.map((opt, idx) => {
          let className = "trivia-option";
          const correctText = question.options[question.correctAnswer - 1];

          if (showResult) {
            if (opt === correctText) className += " correct";
            else if (opt === selected) className += " wrong";
          }

          return (
            <button
              key={idx}
              className={className}
              onClick={() => !showResult && handleAnswer(opt)}
              disabled={showResult}
            >
              {opt}
            </button>
          );
        })}
      </div>
    </div>
  );
}
