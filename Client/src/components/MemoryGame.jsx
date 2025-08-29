import React, { useState, useEffect } from "react";
import { checkAnswer } from "../services/MemoryGameServiceRMI";
import { getCurrentUser } from "../services/userService";
import { flipQuestion, flipAnswer } from "../services/MemoryGameServiceRMI";
import { getMemoryGameState } from "../services/MemoryGameServiceRMI";
import { useUserContext } from "../context/UserContext";
import { getGameResult } from "../services/MemoryGameServiceRMI";
import EndOfMemoryPage from "../pages/EndOfMemoryPage";
import "../styles/MemoryGame.css";
import logo from "../assets/logo1.png";

export default function memoryGame({ questions, sessionId, opponent }) {
  const [currentPlayer, setCurrentPlayer] = useState(null);
  const [showResult, setShowResult] = useState(false);
  const [scoreResult, setScoreResult] = useState({ user1: 0, user2: 0 });
  const { setUser } = useUserContext();
  useEffect(() => {
    getCurrentUser()
      .then((user) => {
        setUser(user);
      })
      .catch(() => {
        setUser(null);
        console.log("No user found in session- MemoryGame.");
      });
  }, []);
  const { user } = useUserContext();
  const userEmail = user?.email || "guest@example.com";

  const [questionMatched, setQuestionMatched] = useState([]);
  const [answerMatched, setAnswerMatched] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(-1);
  const [currentAnswer, setCurrentAnswer] = useState(-1);
  const [questionStatus, setQuestionStatus] = useState({});
  const [answerStatus, setAnswerStatus] = useState({});
  const [flippedQuestionCards, setFlippedQuestionCards] = useState(
    Array(questions.length / 2).fill(false)
  );
  const [flippedAnswerCards, setFlippedAnswerCards] = useState(
    Array(questions.length / 2).fill(false)
  );
  const [questionCards, setQuestionCards] = useState([]);
  const [answerCards, setAnswerCards] = useState([]);

  useEffect(() => {
    // Slice and sort the first 12
    const sortedQuestions = questions
      .slice(0, 12)
      .sort((a, b) => a.cardId - b.cardId);

    // Slice and sort the last 12
    const sortedAnswers = questions
      .slice(12, 24)
      .sort((a, b) => a.cardId - b.cardId);

    setQuestionCards(sortedQuestions);
    setAnswerCards(sortedAnswers);
  }, [questions]);

  const [questionFlipped, setQuestionFlipped] = useState(false);
  const [answerFlipped, setAnswerFlipped] = useState(false);

  const updateQuestionFlip = (i) => {
    setFlippedQuestionCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setQuestionFlipped(true);
    setCurrentQuestion(i);
  };
  const updateAnswerFlip = (i) => {
    setFlippedAnswerCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setAnswerFlipped(true);
    setCurrentAnswer(i);
  };
  const handleQuestionFlip = (i) => {
    if (currentPlayer != userEmail) {
      return;
    }

    const alreadyFlipped = flippedQuestionCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedQuestionCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setQuestionFlipped(true);
    setCurrentQuestion(i);
    flipQuestion({ sessionId, selectedQuestionCard: i });
  };

  const handleAnswerFlip = (i) => {
    if (currentPlayer != userEmail) {
      return;
    }
    const alreadyFlipped = flippedAnswerCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedAnswerCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setAnswerFlipped(true);
    setCurrentAnswer(i);
    flipAnswer({ sessionId, selectedAnswerCard: i });
  };

  useEffect(() => {
    if (questionFlipped && answerFlipped) {
      setTimeout(() => {
        checkAnswer({
          sessionId,
          userEmail,
          currentPlayer,
          selectedQuestionCard: currentQuestion,
          selectedAnswerCard: currentAnswer,
        }).then((isCorrect) => {
          setTimeout(() => {
            setQuestionStatus((prev) => ({
              ...prev,
              [currentQuestion]: isCorrect ? "correct" : "incorrect",
            }));
            setAnswerStatus((prev) => ({
              ...prev,
              [currentAnswer]: isCorrect ? "correct" : "incorrect",
            }));

            if (!isCorrect) {
              // Flip cards back after showing incorrect
              setTimeout(() => {
                setQuestionStatus((prev) => ({
                  ...prev,
                  [currentQuestion]: null,
                }));
                setAnswerStatus((prev) => ({
                  ...prev,
                  [currentAnswer]: null,
                }));

                setFlippedQuestionCards((prev) =>
                  prev.map((flip, i) => (i === currentQuestion ? false : flip))
                );

                setFlippedAnswerCards((prev) =>
                  prev.map((flip, i) => (i === currentAnswer ? false : flip))
                );
              }, 3000); // flip back after 1 sec of red
            }
            if (isCorrect) {
              setQuestionMatched(questionCards[currentQuestion].cardId);
              setAnswerMatched(answerCards[currentAnswer].cardId);
              setShowResult(true);

              setTimeout(() => {
                setShowResult(false);
                setQuestionMatched();
                setAnswerMatched();

                setQuestionStatus((prev) => ({
                  ...prev,
                  [currentQuestion]: "disabled",
                }));
                setAnswerStatus((prev) => ({
                  ...prev,
                  [currentAnswer]: "disabled",
                }));

                setFlippedQuestionCards((prev) =>
                  prev.map((flip, i) => (i === currentQuestion ? false : flip))
                );

                setFlippedAnswerCards((prev) =>
                  prev.map((flip, i) => (i === currentAnswer ? false : flip))
                );
              }, 3000); // green flash for 3 seconds
            }
            1;
          }, 750);
          setTimeout(() => {
            setQuestionFlipped(false);
            setAnswerFlipped(false);
            setCurrentQuestion(-1);
            setCurrentAnswer(-1);
          }, 1000); // same delay as feedback trigger
        });
      }, 1000);
    }
  }, [questionFlipped, answerFlipped]);

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const gameState = await getMemoryGameState(sessionId);
        setCurrentPlayer(gameState.currentPlayer);
        if (gameState.currentPlayer != userEmail) {
          // Update local state based on backend game state
          if (currentQuestion != gameState.flippedQuestion) {
            if (gameState.flippedQuestion != -1)
              updateQuestionFlip(gameState.flippedQuestion);
          }
          if (currentAnswer != gameState.flippedAnswer) {
            if (gameState.flippedAnswer != -1) {
              updateAnswerFlip(gameState.flippedAnswer);
            }
          }
        }
        if (gameState.gameOver) {
          clearInterval(interval);
          console.log("Game over detected in MemoryGameState");
          const finalResult = await getGameResult(sessionId);
          setScoreResult(finalResult);
          console.log("Game over! Final result:", finalResult);

          return (
            <EndOfMemoryPage
              scores={finalResult.scores}
              sessionId={sessionId}
            />
          );
        }
      } catch (err) {
        console.error("Polling error in MemoryGameState:", err);
      }
    }, 1000); // poll every second

    return () => clearInterval(interval); // cleanup on unmount
  }, [sessionId]);

  return (
    <div className="board-wrapper">
      <div className="player_A">
        {user.picture ? (
          <img
            src={user.picture}
            alt="Player A"
            className={`profile-pic ${
              user.email === currentPlayer ? "active-player" : ""
            }`}
            onError={(e) => {
              e.target.onerror = null;
              e.target.style.display = "none";
              e.target.nextSibling.style.display = "flex";
            }}
          />
        ) : (
          <div
            className={`fallback-avatar ${
              user.email === currentPlayer ? "active-player" : ""
            }`}
          >
            {user.email.slice(0, 2).toUpperCase()}
          </div>
        )}
        <div className="player-email">{user.name ? user.name : user.email}</div>
      </div>

      <div className="memory_game">
        {showResult && <div className="score-popup">+1</div>}
        <div className="board">
          <div className="question-header">
            <h2>שאלות</h2>
            <div className="questions-board">
              {questionCards.map((card, i) => (
                <div
                  key={card.cardId}
                  className={`card ${
                    questionMatched === card.cardId
                      ? "correct"
                      : questionStatus[card.cardId] === "incorrect"
                      ? "incorrect"
                      : questionStatus[card.cardId] === "disabled"
                      ? "disabled"
                      : ""
                  }`}
                  onClick={() => handleQuestionFlip(i)}
                >
                  <div
                    className={`card-inner ${
                      flippedQuestionCards[i] ? "flipped" : ""
                    }`}
                  >
                    <div className="card-front">
                      <img src={logo} alt="EZDrive Logo" className="logo_img" />
                    </div>
                    <div className="card-back">
                      {questionStatus[card.cardId] !== "disabled" && (
                        <>
                          <span className="card-text">{card.text}</span>
                          {card.imageUrl && (
                            <img
                              src={
                                card.imageUrl.startsWith("http")
                                  ? card.imageUrl
                                  : `https://${card.imageUrl}`
                              }
                              alt="שאלה"
                              className="question-image"
                              onError={(e) => {
                                e.target.onerror = null;
                                e.target.src = logo; // fallback to EZDrive logo
                              }}
                            />
                          )}
                        </>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="answer-header">
            <h2>תשובות</h2>
            <div className="answer-board">
              {answerCards.map((card, i) => (
                <div
                  key={card.cardId}
                  className={`card ${
                    answerMatched === card.cardId
                      ? "correct"
                      : answerStatus[card.cardId] === "incorrect"
                      ? "incorrect"
                      : answerStatus[card.cardId] === "disabled"
                      ? "disabled"
                      : ""
                  }`}
                  onClick={() => handleAnswerFlip(i)}
                >
                  <div
                    className={`card-inner ${
                      flippedAnswerCards[i] ? "flipped" : ""
                    }`}
                  >
                    {
                      <div className="card-front">
                        <img
                          src={logo}
                          alt="EZDrive Logo"
                          className="logo_img"
                        />
                      </div>
                    }
                    <div className="card-back">
                      {answerStatus[card.cardId] === "disabled" && (
                        <img
                          src={logo}
                          alt="EZDrive Logo"
                          className="logo_img"
                        />
                      )}
                      {answerStatus[card.cardId] != "disabled" && (
                        <>
                          <span className="card-text">{card.text}</span>
                        </>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="player_B">
        {opponent.picture ? (
          <img
            src={opponent.picture}
            alt="Player B"
            className={`profile-pic ${
              opponent.email === currentPlayer ? "active-player" : ""
            }`}
            onError={(e) => {
              e.target.onerror = null;
              e.target.style.display = "none";
              e.target.nextSibling.style.display = "flex";
            }}
          />
        ) : (
          <div
            className={`fallback-avatar ${
              user.email === currentPlayer ? "active-player" : ""
            }`}
          >
            {user.email.slice(0, 2).toUpperCase()}
          </div>
        )}

        <div className="player-email">
          {opponent.name ? opponent.name : opponent.email}
        </div>
      </div>
    </div>
  );
}
