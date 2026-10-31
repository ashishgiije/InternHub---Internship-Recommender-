const currentUserRecommendations = requireUser();
setupUserLogout();

const recommendationError = document.getElementById("recommendationError");
const suggestedSkills = document.getElementById("suggestedSkills");
const recommendationContainer = document.getElementById("recommendationContainer");
const allInternshipsContainer = document.getElementById("allInternshipsContainer");
const searchInput = document.getElementById("searchInput");
const categoryFilter = document.getElementById("categoryFilter");
const refreshRecommendations = document.getElementById("refreshRecommendations");
let availableInternships = [];

async function applyForInternship(internshipId) {
    recommendationError.textContent = "";
    try {
        const response = await fetch(`${API_BASE_URL}/users/${currentUserRecommendations.userId}/applications/${internshipId}`, {
            method: "POST"
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not apply for internship.");
        }
        showToast("Application submitted successfully.");
    } catch (e) {
        recommendationError.textContent = e.message;
    }
}

function showToast(message) {
    const existing = document.querySelector(".toast");
    if (existing) {
        existing.remove();
    }
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 2200);
}

function renderAllInternships() {
    const searchValue = searchInput.value.trim().toLowerCase();
    const selectedCategory = categoryFilter.value.trim().toLowerCase();
    const filtered = availableInternships.filter((internship) => {
        const matchesCategory = !selectedCategory || (internship.category || "").toLowerCase() === selectedCategory;
        const haystack = `${internship.title} ${internship.company} ${internship.skills} ${internship.category || ""}`.toLowerCase();
        const matchesSearch = !searchValue || haystack.includes(searchValue);
        return matchesCategory && matchesSearch;
    });

    if (filtered.length === 0) {
        allInternshipsContainer.innerHTML = "<div class='card'>No internships match your search.</div>";
        return;
    }

    const groups = {};
    filtered.forEach((item) => {
        const key = item.category || "General";
        groups[key] = groups[key] || [];
        groups[key].push(item);
    });

    allInternshipsContainer.innerHTML = Object.entries(groups).map(([category, items]) => `
        <article class="card">
            <h3>${category}</h3>
            ${items.map((item) => `
                <div class="card">
                    <h4>${item.title}</h4>
                    <p><strong>Company:</strong> ${item.company}</p>
                    <p><strong>Skills:</strong> ${item.skills}</p>
                    <button data-internship-id="${item.id}" class="apply-btn">Apply Internship</button>
                    <a class="button-link secondary-link" href="${item.applyLink}" target="_blank" rel="noopener noreferrer">Open Apply Link</a>
                </div>
            `).join("")}
        </article>
    `).join("");

    document.querySelectorAll(".apply-btn").forEach((button) => {
        button.addEventListener("click", async () => {
            await applyForInternship(button.getAttribute("data-internship-id"));
        });
    });
}

async function loadAllInternships() {
    const response = await fetch(`${API_BASE_URL}/internships`);
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Could not load internships.");
    }
    availableInternships = data;

    const categories = [...new Set(data.map((item) => item.category || "General"))];
    categoryFilter.innerHTML = `<option value="">All Categories</option>${
        categories.map((category) => `<option value="${category}">${category}</option>`).join("")
    }`;
    renderAllInternships();
}

async function loadRecommendations() {
    recommendationError.textContent = "";
    suggestedSkills.innerHTML = "";
    recommendationContainer.innerHTML = "";

    try {
        const user = await fetchUserProfile(currentUserRecommendations.userId);
        if (!user.skills || !user.interests) {
            recommendationError.textContent = "Please update your profile first.";
            return;
        }

        const response = await fetch(`${API_BASE_URL}/recommend`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                skills: user.skills,
                interests: `${user.interests}, ${user.activities || ""}`
            })
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not load recommendations.");
        }

        if (data.suggestedSkills && data.suggestedSkills.length > 0) {
            suggestedSkills.innerHTML = data.suggestedSkills.map((skill) => `<span class="badge">${skill}</span>`).join("");
        }

        if (!data.recommendations || data.recommendations.length === 0) {
            recommendationContainer.innerHTML = "<div class='card'>No recommendations available right now.</div>";
            return;
        }

        recommendationContainer.innerHTML = data.recommendations.map((item) => `
            <article class="card">
                <h3>${item.title}</h3>
                <p><strong>Company:</strong> ${item.company}</p>
                <p><strong>Required Skills:</strong> ${item.requiredSkills}</p>
                <p><strong>Matching Score:</strong> <span class="score-highlight">${item.matchingScore}%</span></p>
                <button data-internship-id="${item.internshipId}" class="apply-rec-btn">Apply Internship</button>
                <a class="button-link secondary-link" href="${item.applyLink}" target="_blank" rel="noopener noreferrer">Open Apply Link</a>
            </article>
        `).join("");

        document.querySelectorAll(".apply-rec-btn").forEach((button) => {
            button.addEventListener("click", async () => {
                await applyForInternship(button.getAttribute("data-internship-id"));
            });
        });
    } catch (e) {
        recommendationError.textContent = e.message;
    }
}

refreshRecommendations.addEventListener("click", loadRecommendations);
searchInput.addEventListener("input", renderAllInternships);
categoryFilter.addEventListener("change", renderAllInternships);

loadRecommendations();
loadAllInternships().catch((e) => {
    recommendationError.textContent = e.message;
});
