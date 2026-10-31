const ADMIN_APPLICATIONS_URL = "http://localhost:8080/api/admin/applications";
const ADMIN_STATUS_URL = "http://localhost:8080/api/admin/applications";

const applicationError = document.getElementById("applicationError");
const applicationTableBody = document.getElementById("applicationTableBody");

async function updateStatus(applicationId, status) {
    try {
        const response = await fetch(`${ADMIN_STATUS_URL}/${applicationId}/status`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                ...adminHeaders()
            },
            body: JSON.stringify({ status })
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Could not update status.");
        }
    } catch (e) {
        applicationError.textContent = e.message;
    }
}

async function loadApplications() {
    applicationError.textContent = "";
    applicationTableBody.innerHTML = "";
    try {
        const response = await fetch(ADMIN_APPLICATIONS_URL, {
            headers: adminHeaders()
        });
        const applications = await response.json();
        if (!response.ok) {
            throw new Error(applications.message || "Could not load applications.");
        }

        if (applications.length === 0) {
            applicationTableBody.innerHTML = "<tr><td colspan='8'>No applications yet.</td></tr>";
            return;
        }

        applicationTableBody.innerHTML = applications.map((item) => `
            <tr>
                <td>${item.userName}</td>
                <td>${item.userEmail}</td>
                <td>${item.internshipTitle}</td>
                <td>${item.company}</td>
                <td>${item.category || "General"}</td>
                <td>${item.status}</td>
                <td>${new Date(item.appliedAt).toLocaleString()}</td>
                <td>
                    <select class="status-select" data-id="${item.id}">
                        <option value="APPLIED" ${item.status === "APPLIED" ? "selected" : ""}>APPLIED</option>
                        <option value="UNDER_REVIEW" ${item.status === "UNDER_REVIEW" ? "selected" : ""}>UNDER_REVIEW</option>
                        <option value="SHORTLISTED" ${item.status === "SHORTLISTED" ? "selected" : ""}>SHORTLISTED</option>
                        <option value="REJECTED" ${item.status === "REJECTED" ? "selected" : ""}>REJECTED</option>
                    </select>
                </td>
            </tr>
        `).join("");

        document.querySelectorAll(".status-select").forEach((select) => {
            select.addEventListener("change", async () => {
                await updateStatus(select.getAttribute("data-id"), select.value);
                await loadApplications();
            });
        });
    } catch (e) {
        applicationError.textContent = e.message;
    }
}

loadApplications();
