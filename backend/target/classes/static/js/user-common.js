const API_BASE_URL = "http://localhost:8080/api";

function getStoredUser() {
    return JSON.parse(localStorage.getItem("loggedInUser") || "null");
}

function setStoredUser(user) {
    localStorage.setItem("loggedInUser", JSON.stringify(user));
}

function requireUser() {
    const user = getStoredUser();
    if (!user || !user.userId) {
        window.location.href = "login.html";
        throw new Error("User not logged in.");
    }
    return user;
}

async function fetchUserProfile(userId) {
    const response = await fetch(`${API_BASE_URL}/users/${userId}`);
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Could not fetch user profile.");
    }
    setStoredUser(data);
    return data;
}

function setupUserLogout() {
    const logoutBtn = document.getElementById("logoutBtn");
    if (!logoutBtn) {
        return;
    }
    logoutBtn.addEventListener("click", (event) => {
        event.preventDefault();
        localStorage.removeItem("loggedInUser");
        window.location.href = "login.html";
    });
}
