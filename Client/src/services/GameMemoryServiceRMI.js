export async function startMemorySession(category) {
  const res = await fetch("http://localhost:8080/game-sessions/startMemory", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({
      gameType: "memory",
      category,
    }),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`startMemorySession failed: ${res.status} - ${text}`);
  }

  return await res.json();
}

export async function joinMemoryGame(sessionId) {
  const res = await fetch(
    `http://localhost:8080/game-sessions/join-memory?sessionId=${sessionId}`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    }
  );
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`joinMemoryGame failed: ${res.status} - ${text}`);
  }
  return await res.json();
}

export function checkAnswer({
  sessionId,
  currentPlayer,
  selectedQuestionCard,
  selectedAnswerCard,
}) {
  return fetch("http://localhost:8080/game-sessions/check-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({
      sessionId,
      currentPlayer,
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

export async function getMemoryGameStatus(sessionId) {
  const response = await fetch(
    `http://localhost:8080/game-sessions/memory-status?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!response.ok) {
    throw new Error("Failed to get memory game status");
  }
  return await response.json();
}

export function flipQuestion({ sessionId, selectedQuestionCard }) {
  return fetch(
    `http://localhost:8080/game-sessions/flip-question?sessionId=${sessionId}&questionIndex=${selectedQuestionCard}`,
    {
      method: "POST",
      credentials: "include",
    }
  );
}
export function flipAnswer({ sessionId, selectedAnswerCard }) {
  return fetch(
    `http://localhost:8080/game-sessions/flip-answer?sessionId=${sessionId}&answerIndex=${selectedAnswerCard}`,
    {
      method: "POST",
      credentials: "include",
    }
  );
}
export async function getMemoryGameState(sessionId) {
  const response = await fetch(
    `http://localhost:8080/game-sessions/memory-state?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!response.ok) {
    throw new Error("Failed to get memory game state");
  }
  return await response.json();
}
export function getGameResult(sessionId) {
  return fetch(
    `http://localhost:8080/game-sessions/result-memory?sessionId=${sessionId}`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}
