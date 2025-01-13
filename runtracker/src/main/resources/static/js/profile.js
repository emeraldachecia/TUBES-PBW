document.addEventListener("DOMContentLoaded", () => {
	const editButton = document.getElementById("edit-button");
	const deleteButton = document.getElementById("delete-button");
	const logoutButton = document.getElementById("logout-button");
	const popup = document.getElementById("popup-form");
	const cancelButton = document.getElementById("cancel");
	const form = document.getElementById("edit-form");
	const nameField = document.getElementById("name");
	const emailField = document.getElementById("email");
	const profileName = document.getElementById("profile-name");
	const profileEmail = document.getElementById("profile-email");
	const currentPasswordField = document.getElementById("current-password");
	const newPasswordField = document.getElementById("new-password");
	const confirmPasswordField = document.getElementById("confirm-password");

	// Show popup
	editButton.addEventListener("click", () => {
		popup.style.display = "flex";
	});

	// Close popup
	cancelButton.addEventListener("click", () => {
		popup.style.display = "none";
	});

	// Handle form submission
	form.addEventListener("submit", (event) => {
		event.preventDefault();

		// Validate passwords
		if (newPasswordField.value !== confirmPasswordField.value) {
			alert("New password and confirmation password do not match!");
			return;
		}

		// Construct form data to send
		const formData = new FormData(form);

		// Send the data using fetch to handle the response
		fetch("/user/profile", {
			method: "PUT",
			body: formData,
		})
			.then((response) => {
				if (response.ok) {
					return response.text();
				} else {
					throw new Error("Something went wrong on the server.");
				}
			})
			.then((data) => {
				alert("Profile updated successfully!");
				popup.style.display = "none";
				profileName.textContent = nameField.value; // Update the UI with new name
				profileEmail.textContent = emailField.value; // Update the UI with new email
			})
			.catch((error) => {
				alert(error.message);
			});
	});

	// Handle delete account
	deleteButton.addEventListener("click", () => {
		if (
			confirm(
				"Are you sure you want to delete your account? This action cannot be undone."
			)
		) {
			fetch("/user/profile", {
				method: "DELETE",
			})
				.then((response) => {
					if (response.ok) {
						alert("Account deleted successfully.");
						window.location.href = "/user/signin"; // Redirect to sign-in page
					} else {
						alert("Failed to delete account.");
					}
				})
				.catch((error) => {
					alert("Error: " + error.message);
				});
		}
	});

	// Handle log out
	logoutButton.addEventListener("click", () => {
		fetch("/user/signout", {
			method: "POST",
		})
			.then((response) => {
				if (response.ok) {
					window.location.href = "/user/signin"; // Redirect to sign-in page
				} else {
					alert("Failed to log out.");
				}
			})
			.catch((error) => {
				alert("Error: " + error.message);
			});
	});
});
