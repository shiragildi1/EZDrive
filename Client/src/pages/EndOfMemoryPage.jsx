import "../styles/EndOfMemory.css";
import { Link } from "react-router-dom";
import { useState } from "react";

export default function EndOfMemoryPage({ score1, score2, sessionId }) {
  return (
    <div className="end-of-memory-page">
      <div className="end-of-memory-header">
        <h1>!סיימתם את משחק הזיכרון</h1>
        <h2>הנה התוצאות </h2>
      </div>
      <div className="squares-end-of-memory">
        <div className="square-end-of-memory">
          <h3>ניקוד</h3>
          <div className="score1">
            <p>{score}%</p>
          </div>
        </div>
      </div>
      <div className="buttons-end-of-memory">
        <div className="left-buttons">
          <Link to="/HomePage" className="no-style-link">
            <button className="menu-button1">
              <h3>חזרה לעמוד הבית</h3>
            </button>
          </Link>

          <Link to="/HomePage" className="no-style-link">
            <button className="play-again-button">
              <h3>שחק שוב</h3>
            </button>
          </Link>
        </div>
      </div>
      {showPopup && (
        <YourTriviaAnswer sessionId={sessionId} onClose={handleClose} />
      )}
    </div>
  );
}