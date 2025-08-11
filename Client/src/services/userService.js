export async function getCurrentUser() {
  try {
    const res = await fetch("http://localhost:8080/api/user/me", {
      method: "GET",
      credentials: "include",
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(`getCurrentUser failed: ${res.status} - ${text}`);
    }

    return await res.json();
  } catch (error) {
    console.error("getCurrentUser error:", error.message);
    throw error;
  }
}
