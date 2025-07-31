
export function sendEmailForOtp(email) {
  return fetch("http://localhost:8080/api/otp/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
  }).then((res) => res.json());
}

export function sendCodeForOtp(code, email) {
  return fetch("http://localhost:8080/api/otp/verify", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, code }),
    credentials: "include",
  }).then((res) => res.json())
}
