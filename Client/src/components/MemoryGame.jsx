import React, { useState, useEffect } from "react";
import { checkAnswer } from "../services/GameMemoryServiceRMI";
import { getCurrentUser } from "../services/userService";
import { useUserContext } from "../context/UserContext";
import "../styles/MemoryGame.css";
import logo from "../assets/logo1.png";

function shuffleArray(array) {
  const shuffled = [...array];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }
  return shuffled;
}

export default function memoryGame({ questions, sessionId, topic }) {
  const [playerA, setPlayerA] = useState("pessyisraeli@gmail.com");
  const [playerB, setPlayerB] = useState("pessyisraeli@gmail.com");
  const [currentPlayer, setCurrentPlayer] = useState("pessy@example.com");
  const [showResult, setShowResult] = useState(false);
  const [gameOver, setGameOver] = useState(false);
  const [scoreResult, setScoreResult] = useState({ a: 0, b: 0 });
  const { setUser } = useUserContext();
  useEffect(() => {
    getCurrentUser()
      .then((user) => {
        setUser(user);
        console.log("User loaded from session - MemoryGame:", user);
      })
      .catch(() => {
        setUser(null);
        console.log("No user found in session- MemoryGame.");
      });
  }, []);
  const { user } = useUserContext();
  const userEmail = user?.email || "guest@example.com";

  const [justMatched, setJustMatched] = useState([]);
  const [found, setFound] = useState(Array(questions.length).fill(false));
  const [currentQuestion, setCurrentQuestion] = useState(-1);
  const [currentAnswer, setCurrentAnswer] = useState(-1);
  const [cardStatus, setCardStatus] = useState(
    Array(questions.length).fill(null)
  );
  const [flippedQuestionCards, setFlippedQuestionCards] = useState(
    Array(questions.length / 2).fill(false)
  );
  const [flippedAnswerCards, setFlippedAnswerCards] = useState(
    Array(questions.length / 2).fill(false)
  );
  // console.log("Questions length:", questions.length);

  const [questionCards, setQuestionCards] = useState([]);
  const [answerCards, setAnswerCards] = useState([]);

  useEffect(() => {
    setQuestionCards(shuffleArray(questions.slice(0, 12)));
    setAnswerCards(shuffleArray(questions.slice(12, 24)));
  }, []);
  const [questionFlipped, setQuestionFlipped] = useState(false);
  const [answerFlipped, setAnswerFlipped] = useState(false);

  const handleQuestionFlip = (i) => {
    const alreadyFlipped = flippedQuestionCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedQuestionCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setQuestionFlipped(true);
    console.log("qu:" + questionCards[i].cardId);
    setCurrentQuestion(questionCards[i].cardId);
  };
  const handleAnswerFlip = (i) => {
    const alreadyFlipped = flippedAnswerCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedAnswerCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setAnswerFlipped(true);
    setCurrentAnswer(answerCards[i].cardId);
  };

  useEffect(() => {
    if (questionFlipped && answerFlipped) {
      console.log("Q: " + currentQuestion + " A: " + currentAnswer);
      console.log("User loaded from session - MemoryGame:", user);
      checkAnswer({
        sessionId,
        userEmail,
        selectedQuestionCard: currentQuestion,
        selectedAnswerCard: currentAnswer,
      }).then((isCorrect) => {
        setTimeout(() => {
          setCardStatus((prev) =>
            prev.map((status, i) =>
              i === currentQuestion || i === currentAnswer
                ? isCorrect
                  ? "correct"
                  : "incorrect"
                : status
            )
          );
          if (!isCorrect) {
            // Flip cards back after showing incorrect
            setTimeout(() => {
              setCardStatus((prev) =>
                prev.map((status) =>
                  status === "disabled" ? "disabled" : null
                )
              );
              setFlippedQuestionCards((prev) =>
                prev.map((flip, i) =>
                  i ===
                  questionCards.findIndex((q) => q.cardId === currentQuestion)
                    ? false
                    : flip
                )
              );

              setFlippedAnswerCards((prev) =>
                prev.map((flip, i) =>
                  i === answerCards.findIndex((a) => a.cardId === currentAnswer)
                    ? false
                    : flip
                )
              );
            }, 3000); // flip back after 1 sec of red
          }
          if (isCorrect) {
            setJustMatched([currentQuestion, currentAnswer]);
            setShowResult(true);

            setTimeout(() => {
              setShowResult(false);
              setJustMatched([]);
              setCardStatus((prev) =>
                prev.map((status, i) =>
                  i === currentQuestion || i === currentAnswer
                    ? "disabled"
                    : status
                )
              );
              setFlippedQuestionCards((prev) =>
                prev.map((flip, i) =>
                  i ===
                  questionCards.findIndex((q) => q.cardId === currentQuestion)
                    ? false
                    : flip
                )
              );

              setFlippedAnswerCards((prev) =>
                prev.map((flip, i) =>
                  i === answerCards.findIndex((a) => a.cardId === currentAnswer)
                    ? false
                    : flip
                )
              );
            }, 3000); // green flash for 1 second

            setFound((prev) => {
              const updated = [...prev];
              updated[currentQuestion] = true;
              updated[currentAnswer] = true;
              return updated;
            });
          }

          console.log("Received from backend:", isCorrect);
          1;
        }, 750);
        setTimeout(() => {
          setQuestionFlipped(false);
          setAnswerFlipped(false);
        }, 1000); // same delay as feedback trigger
      });
    }
  }, [questionFlipped, answerFlipped]);

  return (
    <div className="board-wrapper">
      <div className="player_A">Player A</div>
      <div className="memory_game">
        {showResult && <div className="score-popup">+1</div>}
        {/* <div className="header">
        <div className="score"> </div>
       </div> */}

        <div className="board">
          <div className="questions-board">
            {questionCards.map((card, i) => (
              <div
                key={card.cardId}
                className={`card ${
                  justMatched.includes(card.cardId)
                    ? "correct"
                    : cardStatus[card.cardId] === "incorrect"
                    ? "incorrect"
                    : cardStatus[card.cardId] === "disabled"
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
                    {cardStatus[card.cardId] === "disabled" && (
                      <img src={logo} alt="EZDrive Logo" className="logo_img" />
                    )}
                    {cardStatus[card.cardId] != "disabled" && (
                      <span className="card-text">{card.text}</span>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="answer-board">
            {answerCards.map((card, i) => (
              <div
                key={card.cardId}
                className={`card ${
                  justMatched.includes(card.cardId)
                    ? "correct"
                    : cardStatus[card.cardId] === "incorrect"
                    ? "incorrect"
                    : cardStatus[card.cardId] === "disabled"
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
                  <div className="card-front">
                    <img src={logo} alt="EZDrive Logo" className="logo_img" />
                  </div>
                  <div className="card-back">
                    {cardStatus[card.cardId] === "disabled" && (
                      <img src={logo} alt="EZDrive Logo" className="logo_img" />
                    )}
                    {cardStatus[card.cardId] != "disabled" && (
                      <span className="card-text">{card.text}</span>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
      <div className="player_B">Player B</div>
    </div>
  );
}
