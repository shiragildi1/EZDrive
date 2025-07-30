import axios from "axios";

export function startMemorySession(userEmail, userEmail2, category) {
  console.log("email", userEmail, userEmail2, category);
  return fetch(
    `http://localhost:8080/game-sessions/startMemory?userEmail=${userEmail}&userEmail2=${userEmail2}&gameType=memory&category=${category}`,
    { method: "POST" }
  ).then((res) => res.json());
}

export function checkAnswer({
  sessionId,
  userEmail,
  selectedQuestionCard,
  selectedAnswerCard,
}) {
  return axios
    .post("http://localhost:8080/game-sessions/check-answer", {
      // method: "POST",
      // headers: { "Content-Type": "application/json" },
      // body: JSON.stringify({
      sessionId,
      userEmail,
      selectedQuestionCard,
      selectedAnswerCard,
      // }),
    })
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      console.log(sessionId, selectedQuestionCard, selectedAnswerCard);
      throw new Error("Backend error: " + err.message);
    });
}
