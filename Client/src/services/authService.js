export function sendGoogleToken(token) {
  return fetch("http://localhost:8080/api/auth/google", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ token }),
  }).then((res) => res.json());
}
