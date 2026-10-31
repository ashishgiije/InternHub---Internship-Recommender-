const API_BASE_URL = "http://localhost:8080/api";
const adminPass = localStorage.getItem("adminPass");

if (!adminPass) {
    window.location.href = "admin-login.html";
}

function adminHeaders() {
    return { "X-Admin-Pass": adminPass };
}

const adminLogoutBtn = document.getElementById("adminLogoutBtn");
if (adminLogoutBtn) {
    adminLogoutBtn.addEventListener("click", (event) => {
        event.preventDefault();
        localStorage.removeItem("adminPass");
        window.location.href = "admin-login.html";
    });
}
