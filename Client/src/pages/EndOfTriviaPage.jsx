import "../styles/EndOfTrivia.css";
import { Link } from "react-router-dom";
import { useState } from "react";
import YourTriviaAnswer from "./YourTriviaAnswer";

export default function EndOfTriviaPage({
  score,
  numberOfQuestions,
  testLength,
  sessionId,
}) {
  const [showPopup, setShowPopup] = useState(false);

  const handleClose = () => {
    setShowPopup(false);
  };
  const handleOpen = () => {
    setShowPopup(true);
  };

  return (
    <div className="end-of-trivia-page">
      <div className="end-of-trivia-header">
        <div className="end-of-trivia-header-1"> !סיימת את הטריוויה</div>
        <div className="end-of-trivia-header-2">הנה התוצאות שלך</div>
      </div>
      <div className="squares-end-of-trivia">
        <div className="square-end-of-trivia">
          <h3>ניקוד</h3>
          <div className="score">
            <p>{score}%</p>
          </div>
        </div>
        <div className="square-end-of-trivia">
          <h3>מספר התשובות שענית נכון</h3>
          <div className="number-of-questions">
            <p>{numberOfQuestions}/10</p>
          </div>
        </div>
        <div className="square-end-of-trivia">
          <h3>אורך המבחן</h3>
          <div className="test-length">
            <p>{testLength}</p>
          </div>
        </div>
      </div>
      <div className="buttons-end-of-trivia">
        <div className="left-buttons">
          <Link to="/HomePage" className="no-style-link">
            <button className="menu-button1">
              <h3>חזרה לעמוד הבית</h3>
            </button>
          </Link>
        </div>
        <div className="right-buttons">
          <button className="show-answers-button" onClick={handleOpen}>
            <h3>הצגת תשובות</h3>
          </button>

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
