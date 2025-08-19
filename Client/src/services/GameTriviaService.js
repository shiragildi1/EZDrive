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
  console.log("submitAnswer called with:", { sessionId, questionId, selectedAnswer });
  return fetch("http://localhost:8080/game-sessions/submit-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ sessionId, questionId, selectedAnswer }),
  })
    .then((res) => {
      console.log("submitAnswer response status:", res.status);
      return res.text().then((text) => {
        console.log("submitAnswer response text:", text);
        if (!res.ok) {
          throw new Error(`submitAnswer failed: ${res.status} - ${text}`);
        }
        return text ? JSON.parse(text) : null;
      });
    })
    .catch((err) => {
      console.error("submitAnswer error:", err);
      throw err;
    });
}

export function getGameResult(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/result?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}

export function getGameSummary(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/summary?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}
