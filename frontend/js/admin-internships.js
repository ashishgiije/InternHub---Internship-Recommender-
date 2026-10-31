const ADMIN_API_URL = "http://localhost:8080/api/admin/internships";

const internshipForm = document.getElementById("internshipForm");
const internshipId = document.getElementById("internshipId");
const adminMessage = document.getElementById("adminMessage");
const adminError = document.getElementById("adminError");
const tableBody = document.getElementById("tableBody");
const adminSearchInput = document.getElementById("adminSearchInput");
const adminCategoryFilter = document.getElementById("adminCategoryFilter");
let internshipsCache = [];

function renderInternships() {
    const search = adminSearchInput.value.trim().toLowerCase();
    const category = adminCategoryFilter.value.trim().toLowerCase();
    const filtered = internshipsCache.filter((internship) => {
        const matchesCategory = !category || (internship.category || "").toLowerCase() === category;
        const haystack = `${internship.title} ${internship.company} ${internship.skills} ${internship.category || ""}`.toLowerCase();
        const matchesSearch = !search || haystack.includes(search);
        return matchesCategory && matchesSearch;
    });

    tableBody.innerHTML = filtered.map((internship) => `
        <tr>
            <td>${internship.id}</td>
            <td>${internship.title}</td>
            <td>${internship.company}</td>
            <td>${internship.skills}</td>
            <td>${internship.category || "General"}</td>
            <td><a href="${internship.applyLink}" target="_blank" rel="noopener noreferrer">Link</a></td>
            <td>
                <button data-edit-id="${internship.id}" class="edit-btn">Edit</button>
                <button data-delete-id="${internship.id}" class="danger delete-btn">Delete</button>
            </td>
        </tr>
    `).join("");

    document.querySelectorAll(".edit-btn").forEach((button) => {
        button.addEventListener("click", () => editInternship(Number(button.getAttribute("data-edit-id"))));
    });
    document.querySelectorAll(".delete-btn").forEach((button) => {
        button.addEventListener("click", () => deleteInternship(Number(button.getAttribute("data-delete-id"))));
    });
}

async function loadInternships() {
    adminError.textContent = "";
    tableBody.innerHTML = "";

    try {
        const response = await fetch(ADMIN_API_URL, {
            headers: adminHeaders()
        });
        const internships = await response.json();
        if (!response.ok) {
            throw new Error(internships.message || "Could not load internships.");
        }

        internshipsCache = internships;
        const categories = [...new Set(internships.map((item) => item.category || "General"))];
        adminCategoryFilter.innerHTML = `<option value="">All Categories</option>${
            categories.map((item) => `<option value="${item}">${item}</option>`).join("")
        }`;
        renderInternships();
    } catch (error) {
        adminError.textContent = error.message;
    }
}

internshipForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    adminMessage.textContent = "";
    adminError.textContent = "";

    const payload = {
        title: document.getElementById("title").value.trim(),
        company: document.getElementById("company").value.trim(),
        skills: document.getElementById("skills").value.trim(),
        category: document.getElementById("category").value.trim(),
        applyLink: document.getElementById("applyLink").value.trim()
    };

    const idValue = internshipId.value;
    const isUpdate = Boolean(idValue);

    try {
        const response = await fetch(isUpdate ? `${ADMIN_API_URL}/${idValue}` : ADMIN_API_URL, {
            method: isUpdate ? "PUT" : "POST",
            headers: {
                "Content-Type": "application/json",
                ...adminHeaders()
            },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Save failed.");
        }

        adminMessage.textContent = isUpdate ? "Internship updated successfully." : "Internship added successfully.";
        internshipForm.reset();
        internshipId.value = "";
        await loadInternships();
    } catch (error) {
        adminError.textContent = error.message;
    }
});

function editInternship(id) {
    const internship = internshipsCache.find((item) => item.id === id);
    if (!internship) {
        adminError.textContent = "Selected internship was not found.";
        return;
    }
    internshipId.value = internship.id;
    document.getElementById("title").value = internship.title;
    document.getElementById("company").value = internship.company;
    document.getElementById("skills").value = internship.skills;
    document.getElementById("category").value = internship.category || "";
    document.getElementById("applyLink").value = internship.applyLink;
}

async function deleteInternship(id) {
    adminMessage.textContent = "";
    adminError.textContent = "";
    if (!confirm(`Delete internship with ID ${id}?`)) {
        return;
    }
    try {
        const response = await fetch(`${ADMIN_API_URL}/${id}`, {
            method: "DELETE",
            headers: adminHeaders()
        });
        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.message || "Delete failed.");
        }
        adminMessage.textContent = "Internship deleted successfully.";
        await loadInternships();
    } catch (error) {
        adminError.textContent = error.message;
    }
}

adminSearchInput.addEventListener("input", renderInternships);
adminCategoryFilter.addEventListener("change", renderInternships);

loadInternships();
