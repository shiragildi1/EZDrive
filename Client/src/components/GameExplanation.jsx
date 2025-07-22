import React from "react";
import "../styles/GameExplanation.css";

export default function GameExplanation({
  title,
  description,
  sections,
  onStart,
  onClose,
}) {
  return (
    <div className="popup-backdrop">
      <div className="popup-box">
        <button className="close-button" onClick={onClose}>
          X
        </button>

        <h2>{title}</h2>
        <p>{description}</p>

        {sections.map((s, i) => (
          <div key={i} className="section">
            <span className="icon">{s.icon}</span>
            <div>
              <h4>{s.heading}</h4>
              <p>{s.content}</p>
            </div>
          </div>
        ))}

        <button className="start-btn" onClick={onStart}>
          התחל משחק
        </button>
      </div>
    </div>
  );
}
