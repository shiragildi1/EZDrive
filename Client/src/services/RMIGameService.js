export function joinGame(userEmail, userEmail2, category) {
  console.log("email", userEmail, userEmail2, category);
  return fetch(`http://localhost:8080/api/rmi-game/join`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ userEmail, userEmail2, category }),
  }).then((res) => res.json());
}
