export function startTriviaSession(userEmail, category) {
  console.log("email", userEmail, category);
  return fetch(
    `http://localhost:8080/game-sessions/start?userEmail=${userEmail}&gameType=trivia&category=${category}`,
    { method: "POST" }
  ).then((res) => res.json());
}

export function submitAnswer({ sessionId, questionId, selectedAnswer }) {
  return fetch("http://localhost:8080/game-sessions/submit-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
  });
}

export function getGameResult(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/result?sessionId=${sessionId}`
  ).then((res) => res.json());
}
