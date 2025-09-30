// export function sendGoogleToken(idToken) {
//   return fetch("http://localhost:8080/api/auth/google", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     credentials: "include",
//     body: JSON.stringify({ token: idToken }),
//   }).then((res) => {
//     if (!res.ok) throw new Error("Login failed");
//     return res.json(); 
//   });
// }

// export function saveUser(email) {
//   return fetch("http://localhost:8080/api/auth/email", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({ email }),
//     credentials: "include",
//   }).then((res) => res.json());
// }

const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8080";

export function sendGoogleToken(idToken) {
  return fetch(`${API_BASE}/api/auth/google`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ token: idToken }),
  }).then((res) => {
    if (!res.ok) throw new Error("Login failed");
    return res.json();
  });
}

export function saveUser(email) {
  return fetch(`${API_BASE}/api/auth/email`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
    credentials: "include",
  }).then((res) => res.json());
}
