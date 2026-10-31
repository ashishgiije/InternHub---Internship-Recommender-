const currentUser = requireUser();
setupUserLogout();

const welcomeTitle = document.getElementById("welcomeTitle");
const profileCompletion = document.getElementById("profileCompletion");
const recommendationCount = document.getElementById("recommendationCount");
const applicationCount = document.getElementById("applicationCount");

welcomeTitle.textContent = `Welcome, ${currentUser.name}`;

async function loadDashboardStats() {
    const user = await fetchUserProfile(currentUser.userId);
    const profileFields = [user.skills, user.interests, user.activities];
    const completed = profileFields.filter((field) => field && field.trim()).length;
    profileCompletion.textContent = `${Math.round((completed / 3) * 100)}%`;

    const recResponse = await fetch(`${API_BASE_URL}/recommend`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            skills: user.skills || "",
            interests: `${user.interests || ""}, ${user.activities || ""}`
        })
    });
    const recData = await recResponse.json();
    recommendationCount.textContent = recResponse.ok && recData.recommendations ? recData.recommendations.length : "0";

    const appResponse = await fetch(`${API_BASE_URL}/users/${currentUser.userId}/applications`);
    const appData = await appResponse.json();
    applicationCount.textContent = appResponse.ok && Array.isArray(appData) ? appData.length : "0";
}

loadDashboardStats().catch(() => {
    recommendationCount.textContent = "0";
    applicationCount.textContent = "0";
});
