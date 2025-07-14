export function sendGoogleToken(token) {
  return fetch("http://localhost:8080/api/auth/google", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ token }),
    credentials: "include",
  }).then((res) => res.json());
}

export function saveUser(email) {
  return fetch("http://localhost:8080/api/auth/email", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({email}),
  }).then((res) => res.json());
}

