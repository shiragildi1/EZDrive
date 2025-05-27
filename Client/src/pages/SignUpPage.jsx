import React from "react";
import AuthForm from "../components/AuthForm";

export default function LoginPage() {
  return (
    <div
      style={{ display: "flex", justifyContent: "center", marginTop: "100px" }}
    >
      <AuthForm
        title="צור חשבון"
        buttonText="Sign up with email"
        bottomText="Already have an account?"
        link="/SignInPage"
      />
    </div>
  );
}
