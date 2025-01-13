document.addEventListener("DOMContentLoaded", () => {
	setupEventListeners();
	initPagination();
});

function setupEventListeners() {
	const addActivityBtn = document.getElementById("addActivityBtn");
	const cancelActivityBtn = document.getElementById("cancelActivity");
	const activityList = document.getElementById("activityList");

	addActivityBtn.addEventListener("click", () =>
		openModal("activityFormPopup")
	);
	cancelActivityBtn.addEventListener("click", () =>
		closeModal("activityFormPopup")
	);

	activityList.addEventListener("click", (event) => {
		if (event.target.classList.contains("detailBtn")) {
			const activityId = event.target.dataset.activityId;
			showDetail(activityId);
		}
	});

	const deleteDetailActivityBtn = document.getElementById(
		"deleteDetailActivity"
	);
	deleteDetailActivityBtn.addEventListener("click", () => {
		const activityId = document.getElementById("detail-activityForm").dataset
			.activityId;
		if (confirm("Are you sure you want to delete this activity?")) {
			deleteActivity(activityId);
			closeModal("activityDetailPopup");
		}
	});
}

function openModal(modalId) {
	document.getElementById(modalId).style.display = "flex";
}

function closeModal(modalId) {
	document.getElementById(modalId).style.display = "none";
}

function showDetail(activityId) {
	fetch(`/activity/${activityId}`)
		.then((response) => response.json())
		.then((activity) => populateFormDetail(activity))
		.catch((error) => console.error("Error fetching activity details:", error));
}

function populateFormDetail(activity) {
	document.getElementById("detail-title").value = activity.title;
	document.getElementById("detail-date").value = activity.date;
	document.getElementById("detail-description").value = activity.description;
	document.getElementById("detail-duration").value = activity.duration;
	document.getElementById("detail-distance").value = activity.distance;

	openModal("activityDetailPopup");
}

function deleteActivity(activityId) {
	fetch(`/activity/${activityId}`, { method: "DELETE" })
		.then(() => {
			console.log("Activity deleted");
			// Optionally, remove the activity from the DOM or refresh the list
		})
		.catch((error) => console.error("Error deleting activity:", error));
}

const pageSize = 7; // Jumlah item per halaman
let currentPage = 1;
let totalPages = 1;

function initPagination() {
	const activityList = document.getElementById("activityList");
	const activities = Array.from(activityList.querySelectorAll("tr"));

	totalPages = Math.ceil(activities.length / pageSize);
	updatePaginationButtons();
	renderPage(activities, currentPage);

	document.getElementById("prevPage").addEventListener("click", () => {
		if (currentPage > 1) {
			currentPage--;
			renderPage(activities, currentPage);
		}
	});

	document.getElementById("nextPage").addEventListener("click", () => {
		if (currentPage < totalPages) {
			currentPage++;
			renderPage(activities, currentPage);
		}
	});
}

function renderPage(activities, page) {
	const activityList = document.getElementById("activityList");
	activityList.innerHTML = "";

	const start = (page - 1) * pageSize;
	const end = start + pageSize;

	activities.slice(start, end).forEach((activity) => {
		activityList.appendChild(activity);
	});

	updatePaginationButtons();
}

function updatePaginationButtons() {
	document.getElementById("prevPage").disabled = currentPage === 1;
	document.getElementById("nextPage").disabled = currentPage === totalPages;
	document.getElementById("pageInfo").textContent = `Page ${currentPage} of ${totalPages}`;
}