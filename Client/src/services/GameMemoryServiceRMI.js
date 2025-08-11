export async function startMemorySession(category) {
  // יוצר סשן חדש ומחזיר אובייקט עם session (האובייקט של הסשן) ו-questions (רשימת השאלות)
  // לכן בפרונט יש להשתמש ב-data.session.id ולא ב-data ישירות!
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
  // מחזיר את כל האובייקט (ולא רק מזהה!)
  return await res.json();
}

export async function joinMemoryGame(sessionId) {
  // שחקן שני מצטרף לסשן קיים
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
  return true;
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
  //מצפה לתשובה: { ready, questions }
  return await response.json();
}
