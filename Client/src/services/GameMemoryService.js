export function startMemorySession(userEmail, category) {
  console.log("email", userEmail, category);
  return fetch(
    `http://localhost:8080/game-sessions/startMemory?userEmail=${userEmail}&gameType=memory&category=${category}`,
    { method: "POST" }
  ).then((res) => res.json());
}

export function checkAnswer({ sessionId, questionId, selectedAnswer }) {
  return fetch("http://localhost:8080/game-sessions/check-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
  });
}
