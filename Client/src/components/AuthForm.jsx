import React, { useState } from "react";
import "../styles/AuthForm.css";
import { FcGoogle } from "react-icons/fc";
import { Link } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";

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
      <div className="divider">or continue with</div>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("credentialResponse", credentialResponse);
          // send token to server
        }}
        onError={() => {
          console.log("Failed");
        }}
      />

      <p className="forgot">
        {bottomText} <Link to={link}>click here</Link>
      </p>

      <p className="term">
        By clicking continue, you agree to our <a href="#">Terms of Service</a>{" "}
        and <a href="#">Privacy Policy</a>
      </p>
    </form>
  );
}
