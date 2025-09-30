
// export function sendEmailForOtp(email) {
//   return fetch("http://localhost:8080/api/otp/create", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({ email }),
//   }).then((res) => res.json());
// }

// export function sendCodeForOtp(code, email) {
//   return fetch("http://localhost:8080/api/otp/verify", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({ email, code }),
//     credentials: "include",
//   }).then((res) => res.json())
// }

const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8080";

export function sendEmailForOtp(email) {
  return fetch(`${API_BASE}/api/otp/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
  }).then((res) => res.json());
}

export function sendCodeForOtp(code, email) {
  return fetch(`${API_BASE}/api/otp/verify`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, code }),
    credentials: "include",
  }).then((res) => res.json());
}

