// export function getRankingeStats() {
//   return fetch(
//     `http://localhost:8080/api/ranking/stats`,
//     {
//       method: "GET",
//       credentials: "include",
//     }
//   ).then((res) => res.json());
// }

const API_BASE = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";
export function getRankingeStats() {
  return fetch(`${API_BASE}/api/ranking/stats`, {
    method: "GET",
    credentials: "include",
  }).then((res) => res.json());
}
