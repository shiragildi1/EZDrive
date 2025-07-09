import React, { useState, useEffect } from "react";
import "../styles/TriviaGame.css";

export default function TriviaGame({ questions, topic, onFinish }) {
  const [current, setCurrent] = useState(0);
  const [selected, setSelected] = useState(null);
  const [showResult, setShowResult] = useState(false);
  const [score, setScore] = useState(0);
  const [timer, setTimer] = useState(30);

  const question = questions[current];
  if (!question) {
    return <div>טוען שאלות...</div>;
  }

  useEffect(() => {
    // הפעלת טיימר שמפחית את הזמן בכל שנייה
    const intervalId = setInterval(() => {
      setTimer((prevTime) => {
        const nextTime = prevTime - 1;

        if (nextTime <= 0) {
          clearInterval(intervalId); // עצירת הטיימר
          handleAnswer(null); // המשתמש לא ענה בזמן
          return 0;
        }

        return nextTime; // ממשיכים להוריד את הזמן
      });
    }, 1000);

    // ניקוי הטיימר כשעוזבים את המסך או מרעננים
    return () => clearInterval(intervalId);
  }, [current]);

  function handleAnswer(option) {
    setSelected(option);
    setShowResult(true);

    const isCorrect = option === question.correctAnswer;
    if (isCorrect) {
      setScore((prev) => prev + 1);
    }

    setTimeout(() => {
      setShowResult(false);
      setSelected(null);

      if (current < questions.length - 1) {
        setCurrent((prev) => prev + 1);
        setTimer(30); // איפוס הטיימר לשאלה הבאה
      } else {
        const finalScore = Math.round((score / questions.length) * 100);
        if (onFinish) onFinish(finalScore);
      }
    }, 3000);
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
        <div className="trivia-question">{question.question}</div>
      </div>

      <div className="trivia-options">
        {question.options.map((opt, idx) => {
          let className = "trivia-option";
          if (showResult) {
            if (opt === question.correctAnswer) className += " correct";
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
