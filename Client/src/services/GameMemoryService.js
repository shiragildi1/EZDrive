export function startMemorySession(category) {
  return fetch("http://localhost:8080/game-sessions/startMemory", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({
      gameType: "memory",
      category,
    }),
  }).then((res) => {
    if (!res.ok) {
      return res.text().then((text) => {
        throw new Error(`startMemorySession failed: ${res.status} - ${text}`);
      });
    }
    return res.json();
  });
}

export function checkAnswer({
  sessionId,
  selectedQuestionCard,
  selectedAnswerCard,
}) {
  return fetch("http://localhost:8080/game-sessions/check-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({
      sessionId,
      selectedQuestionCard,
      selectedAnswerCard,
    }),
  })
    .then((res) => {
      if (!res.ok) {
        return res.text().then((text) => {
          throw new Error(`checkAnswer failed: ${res.status} - ${text}`);
        });
      }
      return res.json();
    })
    .catch((err) => {
      console.log(sessionId, selectedQuestionCard, selectedAnswerCard);
      throw new Error("Backend error: " + err.message);
    });
}
