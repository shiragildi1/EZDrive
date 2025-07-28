export function startMemorySession(userEmail, category) {
  console.log("email", userEmail, category);
  return fetch(
    `http://localhost:8080/game-sessions/startMemory?userEmail=${userEmail}&gameType=memory&category=${category}`,
    { method: "POST" }
  ).then((res) => res.json());
}

export function checkAnswer({
  sessionId,
  userEmail,
  selectedQuestionCard,
  selectedAnswerCard,
}) {
  return fetch("http://localhost:8080/game-sessions/check-answer", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      sessionId,
      userEmail,
      selectedQuestionCard,
      selectedAnswerCard,
    }),
  }).then((res) => {
    if (!res.ok) {
      console.log(sessionId, selectedQuestionCard, selectedAnswerCard);
      throw new Error("Backend error");
    }
    return res.json(); // 👈 This gets the actual boolean value
  });
}
