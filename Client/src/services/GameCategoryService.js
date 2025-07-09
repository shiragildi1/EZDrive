export function getQuestionsByCategory(category) {
  return fetch(`http://localhost:8080/question/category?category=${category}`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  }).then((res) => res.json());
}
