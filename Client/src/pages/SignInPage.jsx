import React from 'react'
import AuthForm from '../components/AuthForm';

export default function SignInPage()
{
    return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '100px' }}>
    <AuthForm
        title="התחברות"
        buttonText="Sign in with email"
        bottomText="forgot password?"
        link= "#"
      />
    </div>
  );
}
