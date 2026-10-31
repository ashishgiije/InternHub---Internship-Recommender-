const API_BASE_URL = "http://localhost:8080/api";

const adminLoginForm = document.getElementById("adminLoginForm");
const message = document.getElementById("message");
const error = document.getElementById("error");

adminLoginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    message.textContent = "";
    error.textContent = "";

    const adminPass = document.getElementById("adminPass").value.trim();

    try {
        const response = await fetch(`${API_BASE_URL}/admin/login`, {
            method: "POST",
            headers: { "X-Admin-Pass": adminPass }
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Admin login failed.");
        }

        localStorage.setItem("adminPass", adminPass);
        message.textContent = "Admin login successful. Redirecting...";
        setTimeout(() => {
            window.location.href = "admin-home.html";
        }, 500);
    } catch (e) {
        error.textContent = e.message;
    }
});
