export function sendGoogleToken(idToken) {
  return fetch("http://localhost:8080/api/auth/google", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include", // הכי חשוב! שומר את ה-session
    body: JSON.stringify({ token: idToken }),
  }).then((res) => {
    if (!res.ok) throw new Error("Login failed");
    return res.json(); // מחזיר את המשתמש
  });
}

export function saveUser(email) {
  return fetch("http://localhost:8080/api/auth/email", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email }),
    credentials: "include",
  }).then((res) => res.json());
}

export function getCurrentUser() {
  return fetch("http://localhost:8080/api/user/me", {
    method: "GET",
    credentials: "include",
  }).then((res) => {
    if (!res.ok) throw new Error("No user in session");
    return res.json();
  });
}