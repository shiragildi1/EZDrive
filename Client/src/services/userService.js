export function getCurrentUser() {
  return fetch("http://localhost:8080/api/user/me", {
    method: "GET",
    credentials: "include", 
  }).then((res) => {
    if (!res.ok) throw new Error("User not logged in");
    return res.json();
  });
}
