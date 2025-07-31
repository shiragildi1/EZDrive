export function startTriviaSession({ userEmail, category }) {
  return fetch("http://localhost:8080/game-sessions/start", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      userEmail,
      gameType: "trivia",
      category,
    }),
  }).then((res) => res.json());
}

export function submitAnswer({ sessionId, questionId, selectedAnswer }) {
  return fetch(`http://localhost:8080/game-sessions/submit-answer`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
  }).then((res) => res.json());
}

export function getGameResult(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/result?sessionId=${sessionId}`
  ).then((res) => res.json());
}

// export function getGameSummary(sessionId) {
//   return fetch(
//     `http://localhost:8080/game-sessions/summary/${sessionId}`
//   ).then((res) => res.json());
// }

export function getGameSummary(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/summary?sessionId=${sessionId}`
  ).then((res) => res.json());
}

