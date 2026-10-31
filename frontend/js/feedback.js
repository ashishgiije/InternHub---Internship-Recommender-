const feedbackForm = document.getElementById("feedbackForm");
const feedbackSuccess = document.getElementById("feedbackSuccess");

feedbackForm.addEventListener("submit", (event) => {
    event.preventDefault();
    feedbackSuccess.textContent = "Thank you for your feedback. We appreciate your input.";
    feedbackForm.reset();
});
