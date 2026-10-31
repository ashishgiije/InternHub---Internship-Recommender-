const currentUserApplications = requireUser();
setupUserLogout();

const applicationsError = document.getElementById("applicationsError");
const applicationsTableBody = document.getElementById("applicationsTableBody");

async function loadApplications() {
    applicationsError.textContent = "";
    applicationsTableBody.innerHTML = "";
    try {
        const response = await fetch(`${API_BASE_URL}/users/${currentUserApplications.userId}/applications`);
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not load applications.");
        }

        if (data.length === 0) {
            applicationsTableBody.innerHTML = "<tr><td colspan='5'>No applied internships found.</td></tr>";
            return;
        }

        applicationsTableBody.innerHTML = data.map((item) => `
            <tr>
                <td>${item.internshipTitle}</td>
                <td>${item.company}</td>
                <td>${item.category || "General"}</td>
                <td>${item.status}</td>
                <td>${new Date(item.appliedAt).toLocaleString()}</td>
            </tr>
        `).join("");
    } catch (e) {
        applicationsError.textContent = e.message;
    }
}

loadApplications();
