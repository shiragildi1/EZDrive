
export function getProfileStats(userEmail, range) {
  return fetch(
    `http://localhost:8080/api/profile/stats?userEmail=${userEmail}&range=${range}`,
    {
      method: "GET",
      credentials: "include",
    }
  ).then((res) => res.json());
}