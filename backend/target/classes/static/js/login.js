const API_BASE_URL = "http://localhost:8080/api";

const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");
const error = document.getElementById("error");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    message.textContent = "";
    error.textContent = "";

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Login failed.");
        }

        localStorage.setItem("loggedInUser", JSON.stringify(data));
        message.textContent = "Login successful. Redirecting...";
        setTimeout(() => {
            window.location.href = "user-dashboard.html";
        }, 500);
    } catch (e) {
        error.textContent = e.message;
    }
});
