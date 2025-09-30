
// export function getProfileStats(userEmail, range) {
//   return fetch(
//     `http://localhost:8080/api/profile/stats?userEmail=${userEmail}&range=${range}`,
//     {
//       method: "GET",
//       credentials: "include",
//     }
//   ).then((res) => res.json());
// }
const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8080";


export function getProfileStats(userEmail, range) {
  return fetch(
    `${API_BASE}/api/profile/stats?userEmail=${userEmail}&range=${range}`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}
