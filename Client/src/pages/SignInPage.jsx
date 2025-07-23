import React from 'react'
import AuthForm from '../components/AuthForm';

export default function SignInPage()
{
    return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '100px' }}>
    <AuthForm
        title="התחברות"
        buttonText="התחבר עם אימייל"
        bottomText="שכחת סיסמא"
        link= "#"
      />
    </div>
  );
}
