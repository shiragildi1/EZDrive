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
  //const [playerA, setPlayerA] = useState("pessyisraeli@gmail.com");
  //const [playerB, setPlayerB] = useState("pessyisraeli@gmail.com");
  //const [currentPlayer, setCurrentPlayer] = useState("pessy@example.com");
  const [showResult, setShowResult] = useState(false);
  //const [gameOver, setGameOver] = useState(false);
  //const [scoreResult, setScoreResult] = useState({ a: 0, b: 0 });
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

  const [questionMatched, setQuestionMatched] = useState([]);
  const [answerMatched, setAnswerMatched] = useState([]);
  const [found, setFound] = useState(Array(questions.length).fill(false));
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
  console.log("Question Cards:", questionCards);

  const [questionFlipped, setQuestionFlipped] = useState(false);
  const [answerFlipped, setAnswerFlipped] = useState(false);

  const handleQuestionFlip = (i) => {
    const alreadyFlipped = flippedQuestionCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedQuestionCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    //updateOpponentFlippedCards(sessionId,i);
    setQuestionFlipped(true);
    setCurrentQuestion(i);
  };
  const handleAnswerFlip = (i) => {
    const alreadyFlipped = flippedAnswerCards.some((flipped) => flipped); // Check if any card is already flipped
    if (alreadyFlipped) return; // If yes, do nothing

    // Otherwise, flip the selected card
    setFlippedAnswerCards((prev) =>
      prev.map((flipped, index) => (index === i ? true : flipped))
    );
    setAnswerFlipped(true);
    setCurrentAnswer(i);
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
                    {questionStatus[card.cardId] === "disabled" && (
                      <img src={logo} alt="EZDrive Logo" className="logo_img" />
                    )}
                    {questionStatus[card.cardId] != "disabled" && (
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
                  <div className="card-front">
                    <img src={logo} alt="EZDrive Logo" className="logo_img" />
                  </div>
                  <div className="card-back">
                    {answerStatus[card.cardId] === "disabled" && (
                      <img src={logo} alt="EZDrive Logo" className="logo_img" />
                    )}
                    {answerStatus[card.cardId] != "disabled" && (
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
