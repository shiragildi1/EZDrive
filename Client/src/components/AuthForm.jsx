import React, { useState } from "react";
import "../styles/AuthForm.css";
import { FcGoogle } from "react-icons/fc";
import { Link } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";
import { sendGoogleToken } from "../services/authService";

export default function AuthForm({ title, buttonText, bottomText, link }) {
  const [email, setEmail] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Email: ", email);
    //Remember add send to the server
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h1>EZDrive</h1>
      <br />
      <br />
      <br />
      <h2>{title}</h2>
      <input
        type="email"
        placeholder="email@domain.com"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <button type="submit">{buttonText}</button>
      <div className="divider">או המשך עם</div>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("id_token:", credentialResponse.credential);
          sendGoogleToken(credentialResponse.credential)
            .then((data) => {
              console.log("Server response", data);
            })
            .catch((err) => {
              console.error("Error sending token to server:", err);
            });
        }}
        onError={() => {
          console.log("Failed");
        }}
        text="signup_with"
      />

      <p className="forgot">
        {bottomText} <Link to={link}>לחץ כאן</Link>
      </p>

      <p className="term">
        בלחיצה על המשך, הנך מסכים <a href="#">לתנאי השירות </a>{" "}
        <a href="#">ולמדיניות הפרטיות </a> שלנו
      </p>
    </form>
  );
}
