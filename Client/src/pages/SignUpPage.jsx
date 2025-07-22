import React from "react";
import AuthForm from "../components/AuthForm";

export default function LoginPage() {
  return (
    <div
      style={{ display: "flex", justifyContent: "center", marginTop: "100px" }}
    >
      <AuthForm
        title="הרשמה"
        buttonText="הירשם עם אימייל"
        bottomText="משתמש קיים"
        link="/SignInPage"
      />
    </div>
  );
}
