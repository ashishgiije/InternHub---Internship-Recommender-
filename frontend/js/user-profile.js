const currentUserProfile = requireUser();
setupUserLogout();

const profileForm = document.getElementById("profileForm");
const profileMessage = document.getElementById("profileMessage");
const profileError = document.getElementById("profileError");
const resumeForm = document.getElementById("resumeForm");
const resumeMessage = document.getElementById("resumeMessage");
const resumeError = document.getElementById("resumeError");

function fillProfile(user) {
    document.getElementById("skills").value = user.skills || "";
    document.getElementById("interests").value = user.interests || "";
    document.getElementById("activities").value = user.activities || "";
}

fetchUserProfile(currentUserProfile.userId)
    .then(fillProfile)
    .catch((e) => {
        profileError.textContent = e.message;
    });

profileForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    profileMessage.textContent = "";
    profileError.textContent = "";

    const payload = {
        skills: document.getElementById("skills").value.trim(),
        interests: document.getElementById("interests").value.trim(),
        activities: document.getElementById("activities").value.trim()
    };

    try {
        const response = await fetch(`${API_BASE_URL}/users/${currentUserProfile.userId}/profile`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not update profile.");
        }
        setStoredUser(data);
        profileMessage.textContent = "Profile updated successfully.";
    } catch (e) {
        profileError.textContent = e.message;
    }
});

resumeForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    resumeMessage.textContent = "";
    resumeError.textContent = "";

    const fileInput = document.getElementById("resumeFile");
    if (!fileInput.files || fileInput.files.length === 0) {
        resumeError.textContent = "Please select a resume file.";
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    try {
        const response = await fetch(`${API_BASE_URL}/users/${currentUserProfile.userId}/resume-extract`, {
            method: "POST",
            body: formData
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not extract data from CV.");
        }

        if (data.extractedSkills) {
            document.getElementById("skills").value = data.extractedSkills;
        }
        if (data.extractedInterests) {
            document.getElementById("interests").value = data.extractedInterests;
        }
        if (data.extractedActivities) {
            document.getElementById("activities").value = data.extractedActivities;
        }
        resumeMessage.textContent = "CV data extracted. Review and save your profile.";
    } catch (e) {
        resumeError.textContent = e.message;
    }
});
