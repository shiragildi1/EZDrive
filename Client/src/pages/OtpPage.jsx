import "../styles/Otp.css";
import { Link, useNavigate } from "react-router-dom";
import React, { useState, useRef } from "react";
import { useLocation } from "react-router-dom";
import { sendCodeForOtp } from "../services/OtpService";

export default function OtpPage() {
  const [otp, setOtp] = useState(Array(6).fill(""));
  const inputsRef = useRef([]);
  const location = useLocation();
  const userEmail = location.state?.userEmail;

  //if there is a numaric and there is no the last inpute  pass the fucuse to the next.
  function handleChange(e, idx) {
    const value = e.target.value;
    if (!/^\d?$/.test(value)) return;
    const newOtp = [...otp];
    newOtp[idx] = value;
    setOtp(newOtp);
    if (value && idx < 5) {
      inputsRef.current[idx + 1].focus();
    }
    if (value && idx == 5) {
      sendCodeForOtp(newOtp.join(""), userEmail);
    }
  }

  function handleKeyDown(e, idx) {
    if (e.key === "Backspace" && !otp[idx] && idx > 0) {
      inputsRef.current[idx - 1].focus();
    }
  }
  return (
    <div
      style={{ display: "flex", justifyContent: "center", marginTop: "100px" }}
    >
      <div className="OTP">
        <h1>EZDrive</h1>
        <br />
        <br />
        <br />
        <div className="rtl">
          <h2>נשלחה אליך סיסמא חד פעמית למייל.</h2>
          <h2>הזן את הקוד שקיבלת כדי להמשיך</h2>
        </div>

        <div className="squares">
          {[...Array(6)].map((_, i) => (
            <input
              key={i}
              type="text"
              maxLength="1"
              className="square"
              value={otp[i] || ""}
              onChange={(e) => handleChange(e, i)}
              onKeyDown={(e) => handleKeyDown(e, i)}
              inputMode="numeric"
              pattern="[0-9]*"
              ref={(el) => (inputsRef.current[i] = el)}
              autoFocus={i === 0}
            />
          ))}
        </div>

        <div className="rtl">
          <div className="send">
            <h5>לא קיבלתי קוד, </h5>
            <p className="sendAgain">
              <Link to="#">שלח שוב</Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
