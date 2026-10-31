const API_BASE_URL = "http://localhost:8080/api";

const registerForm = document.getElementById("registerForm");
const message = document.getElementById("message");
const error = document.getElementById("error");

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    message.textContent = "";
    error.textContent = "";

    const payload = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Registration failed.");
        }

        localStorage.setItem("loggedInUser", JSON.stringify(data));
        message.textContent = "Registration successful. Redirecting...";
        setTimeout(() => {
            window.location.href = "user-dashboard.html";
        }, 500);
    } catch (e) {
        error.textContent = e.message;
    }
});
