export function getRankingeStats() {
  return fetch(
    `http://localhost:8080/api/ranking/stats`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}