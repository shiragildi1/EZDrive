// export async function sendQuestion(question, conversationId) {
//   const body = conversationId ? { question, conversationId } : { question };
//   const res = await fetch("http://localhost:8080/agent/ask", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     credentials: "include",
//     body: JSON.stringify(body),
//   });
//   if (!res.ok) {
//     const text = await res.text();
//     throw new Error(`sendQuestion failed: ${res.status} - ${text}`);
//   }
//   // מחזיר { answer, conversationId }
//   return await res.json();
// }


const API_BASE = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";

export async function sendQuestion(question, conversationId) {
  const body = conversationId ? { question, conversationId } : { question };

  const res = await fetch(`${API_BASE}/agent/ask`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(`sendQuestion failed: ${res.status} - ${text}`);
  }
  return res.json();
}
