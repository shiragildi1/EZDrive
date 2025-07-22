import React, { useState, useEffect } from "react";
import { submitAnswer, getGameResult } from "../services/GameMemoryService";
import "../styles/MemoryGame.css";
import logo from "../assets/logo1.png";

export default function memoryGame({ questions, sessionId, topic }) {
  const [mockquestions, setQuestions] = useState([
    { text: "🐶 Dog1", position: 2 },
    { text: "🐱 Cat1", position: 0 },
    { text: "🐱 Cat", position: 3 },
    { text: "🐶 Dog", position: 1 },
    { text: "🐶 Dog2", position: 4 },
    { text: "🐶 Dog3", position: 5 },
    { text: "🐶 Dog4", position: 6 },
    { text: "🐶 Dog5", position: 7 },
    { text: "🐶 Dog2", position: 8 },
    { text: "🐶 Dog3", position: 9 },
    { text: "🐶 Dog4", position: 10 },
    { text: "🐶 Dog5", position: 11 },
  ]);
  const [current, setCurrent] = useState(0);

  const [selected, setSelected] = useState(null);
  const [showResult, setShowResult] = useState(false);
  const [gameOver, setGameOver] = useState(false);
  const [scoreResult, setScoreResult] = useState({ a: 0, b: 0 });
  // const [flippedCards, setFlippedCards] = useState([30]);
  const [flippedCards, setFlippedCards] = useState(
    Array(questions.length).fill(false)
  );
  const [isFlipped, setIsFlipped] = useState(false);
  //const cards = Array(questions.length).fill(null);
  const questionCards = questions.filter((q) => q.question === true);
  const answerCards = questions.filter((q) => q.question === false);
  //cardPosition, question, text

  // questions.forEach((q) => {
  //   cards[q.cardPosition] = q;
  // });
  // console.log("Sorted:", cards);

  // const handleFlip = (i) => {
  //   setFlippedCards((prev) => (prev.includes(i) ? prev : [...prev, i]));
  // };
  const handleFlip = (i) => {
    setFlippedCards((prev) =>
      prev.map((flipped, index) => (index === i ? !flipped : flipped))
    );
  };
  // const [cards, setCards] = useState({})

  //cards = [{question_text: "מה המהירות המותרת", position: 3, pair_position: 12}, {...}]

  console.log("Current question:");

  function handleCardClick(cardPosition) {}

  return (
    <div className="memory_game">
      {/* <div className="header">
        <div className="player_A"></div>
        <div className="score"> </div>
        <div className="player_B"></div>
      </div> */}

      <div className="board">
        {/* <div className="board-section">
          <h2>שאלות</h2>
          <div className="questions-board">
            {cards.map((card, i) => (
              <div key={i} className="card" onClick={() => handleFlip(i)}>
                <div
                  className={`card-inner ${flippedCards[i] ? "flipped" : ""}`}
                >
                  <div className="card-front">
                    <img src={logo} alt="EZDrive Logo" className="logo_img" />
                  </div>
                  <div className="card-back">
                    <span>{card.text}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="board-section">
          <h2>תשובות</h2>
          <div className="answer-board">
            {cards.map((card, i) => (
              <div key={i} className="card" onClick={() => handleFlip(i)}>
                <div
                  className={`card-inner ${flippedCards[i] ? "flipped" : ""}`}
                >
                  <div className="card-front">
                    <img src={logo} alt="EZDrive Logo" className="logo_img" />
                  </div>
                  <div className="card-back">
                    <span>{card.text}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div> */}
      </div>
      <div className="questions-board">
        {questionCards.map((card) => (
          <div
            key={card.cardPosition}
            className="card"
            onClick={() => handleFlip(card.cardPosition)}
          >
            <div
              className={`card-inner ${
                flippedCards[card.cardPosition] ? "flipped" : ""
              }`}
            >
              <div className="card-front">
                <img src={logo} alt="EZDrive Logo" className="logo_img" />
              </div>
              <div className="card-back">
                <span>{card.text}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="answer-board">
        {answerCards.map((card) => (
          <div
            key={card.cardPosition}
            className="card"
            onClick={() => handleFlip(card.cardPosition)}
          >
            <div
              className={`card-inner ${
                flippedCards[card.cardPosition] ? "flipped" : ""
              }`}
            >
              <div className="card-front">
                <img src={logo} alt="EZDrive Logo" className="logo_img" />
              </div>
              <div className="card-back">
                <span className="text">{card.text}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
