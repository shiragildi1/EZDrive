export async function sendQuestion(question) {
  const res = await fetch("http://localhost:8080/agent/ask", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ question }), // תיקון כאן!
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`sendQuestion failed: ${res.status} - ${text}`);
  }
  return await res.json();
}