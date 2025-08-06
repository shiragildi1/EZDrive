// export function startTriviaSession({ userEmail, category }) {
//   return fetch("http://localhost:8080/game-sessions/start", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({
//       // userEmail,
//       gameType: "trivia",
//       category,
//     }),
//   }).then((res) => res.json());
// }

// export function submitAnswer({ sessionId, questionId, selectedAnswer }) {
//   return fetch(`http://localhost:8080/game-sessions/submit-answer`, {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
//   }).then((res) => res.json());
// }

// export function getGameResult(sessionId) {
//   return fetch(
//     `http://localhost:8080/game-sessions/result?sessionId=${sessionId}`
//   ).then((res) => res.json());
// }

// export function getGameSummary(sessionId) {
//   return fetch(
//     `http://localhost:8080/game-sessions/summary?sessionId=${sessionId}`
//   ).then((res) => res.json());
// }

export function startTriviaSession({ category }) {
  return fetch("http://localhost:8080/game-sessions/start", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include", // חובה
    body: JSON.stringify({
      gameType: "trivia",
      category,
    }),
  }).then((res) => {
    if (!res.ok) {
      return res.text().then((text) => {
        throw new Error(`startTriviaSession failed: ${res.status} - ${text}`);
      });
    }
    return res.json();
  });
}

export function submitAnswer({ sessionId, questionId, selectedAnswer }) {
  return fetch("http://localhost:8080/game-sessions/submit-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include", // חובה גם כאן
    body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
  }).then((res) => res.json());
}

export function getGameResult(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/result?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include", // גם כאן
    }
  ).then((res) => res.json());
}

export function getGameSummary(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/summary?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include", // גם כאן
    }
  ).then((res) => res.json());
}
