import React, { useState, useEffect } from "react";
import { submitAnswer, getGameResult } from "../services/GameTriviaService";
import { getCurrentUser } from "../services/userService";
import { useUserContext } from "../context/UserContext";
import "../styles/TriviaGame.css";
import EndOfTriviaPage from "../pages/EndOfTriviaPage";

export default function TriviaGame({ questions, sessionId, topic }) {
  const { setUser } = useUserContext();
  useEffect(() => {
    getCurrentUser()
      .then((user) => {
        setUser(user);
        console.log("User loaded from session - TriviaGame:", user);
      })
      .catch(() => {
        setUser(null);
        console.log("No user found in session- TriviaGame.");
      });
  }, []);
  const { user } = useUserContext();

  // Current question index
  const [current, setCurrent] = useState(0);
  // State to track the selected answer and whether to show the result
  const [selected, setSelected] = useState(null);
  // State to control the visibility of the result
  const [showResult, setShowResult] = useState(false);
  // Timer for the trivia game
  const [timer, setTimer] = useState(50);
  // State to track if the game is over and to store the score result
  const [gameOver, setGameOver] = useState(false);
  // State to store the score result
  const [scoreResult, setScoreResult] = useState(null);
  //question object for the current question
  const question = questions[current];

  const [correctAnswerIndex, setCorrectAnswerIndex] = useState(null);

  const [isCorrect, setIsCorrect] = useState(null);

  // Log the current question for debugging
  // console.log("Current question:", question);

  // If there are no questions or options, show a loading message
  if (!question || !Array.isArray(question.options)) {
    return <div>טוען שאלות- קצת סבלנות ומתחילים</div>;
  }
  // Effect to handle the timer countdown
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

  // Function to handle the answer selection
  async function handleAnswer(selectedAnswerIndex) {
    setSelected(selectedAnswerIndex); // זה האינדקס של התשובה שנבחרה (0-based)
    setShowResult(true);

    const result = await submitAnswer({
      sessionId,
      questionId: question.questionId,
      selectedAnswer:
        selectedAnswerIndex != null ? selectedAnswerIndex + 1 : null, // המרה ל־1-based לשרת
    });

    setCorrectAnswerIndex(result.correctAnswerIndex - 1); // נניח שהתשובה מהשרת היא 1-based
    setIsCorrect(result.isCorrect);

    setTimeout(async () => {
      setShowResult(false);
      setSelected(null);
      setCorrectAnswerIndex(null);
      setIsCorrect(null);

      if (current < questions.length - 1) {
        setCurrent((prev) => prev + 1);
        setTimer(50);
      } else {
        const finalResult = await getGameResult(sessionId);
        setScoreResult(finalResult);
        setGameOver(true);
      }
    }, 2000);
  }

  // If the game is over and we have a score result, show the end page
  if (gameOver && scoreResult) {
    return (
      <EndOfTriviaPage
        score={scoreResult.score}
        numberOfQuestions={scoreResult.numberOfCorrectAnswers}
        testLength={scoreResult.totalTimeSession}
        sessionId={sessionId}
      />
    );
  }

  // Render the trivia game interface
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

      <div className="trivia-question-row">
        <div className="trivia-image">
          {question.imageUrl && (
            <img
              src={question.imageUrl}
              alt="Question"
              className="trivia-question-image"
            />
          )}
        </div>
        <div className="trivia-question-col">
          <div className="trivia-question-box">
            <div className="trivia-question">{question.questionText}</div>
          </div>
        </div>
      </div>

      <div className="trivia-options">
        {question.options.map((opt, idx) => {
          let className = "trivia-option";

          if (showResult) {
            if (idx === correctAnswerIndex) {
              className += " correct";
            }

            if (
              selected !== null &&
              idx === selected &&
              idx !== correctAnswerIndex
            ) {
              className += " wrong";
            }
          }

          return (
            <button
              key={idx}
              className={className}
              onClick={() => !showResult && handleAnswer(idx)}
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
