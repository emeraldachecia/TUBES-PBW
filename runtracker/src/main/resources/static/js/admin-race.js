document.addEventListener("DOMContentLoaded", function () {
    const upcomingRaces = [
        { name: "Jakarta Marathon", date: "2025-03-15", location: "Jakarta", description: "Marathon tahunan.", status: "Upcoming", members: [] }
    ];
    const ongoingRaces = [
        { name: "Bali Ultra Run", date: "2025-01-15", location: "Bali", description: "Race di Bali.", status: "On-Going", members: [] }
    ];
    const finishedRaces = [
        { name: "Bandung 10K", date: "2024-12-15", location: "Bandung", description: "Race selesai.", status: "Finished", members: [] }
    ];

    const tabs = {
        Upcoming: upcomingRaces,
        "On-Going": ongoingRaces,
        Finished: finishedRaces
    };

    const raceTable = document.getElementById("raceTable");
    const raceModal = document.getElementById("raceModal");
    const closeModal = document.getElementById("closeModal");
    const saveChanges = document.getElementById("saveChanges");
    const cancelChanges = document.getElementById("cancelChanges");

    const renderTable = (status) => {
        const races = tabs[status];
        const tbody = raceTable.querySelector("tbody");
        tbody.innerHTML = "";

        races.forEach((race, index) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${race.name}</td>
                <td>${race.date}</td>
                <td>${race.location}</td>
                <td>${race.status}</td>
                <td><button class="button-detail" data-status="${status}" data-index="${index}">Detail</button></td>
            `;
            tbody.appendChild(row);
        });
    };

    const openModal = (status, index) => {
        const race = tabs[status][index];
        document.getElementById("modalRaceName").innerText = race.name;
        document.getElementById("modalRaceDate").innerText = race.date;
        document.getElementById("modalRaceLocation").innerText = race.location;
        document.getElementById("modalRaceDescription").innerText = race.description;
        document.getElementById("modalRaceStatus").innerText = race.status;

        saveChanges.style.display = "none";
        cancelChanges.style.display = "none";

        const editButton = document.getElementById("editDetails");
        editButton.style.display = "inline-block";
        editButton.onclick = () => enableEdit(race, status, index);

        raceModal.style.display = "flex";
    };

    const enableEdit = (race, status, index) => {
        const fields = {
            name: document.getElementById("modalRaceName"),
            date: document.getElementById("modalRaceDate"),
            location: document.getElementById("modalRaceLocation"),
            description: document.getElementById("modalRaceDescription")
        };

        for (const key in fields) {
            fields[key].innerHTML = `<input type="text" value="${fields[key].innerText}" id="edit${key}">`;
        }

        saveChanges.style.display = "inline-block";
        cancelChanges.style.display = "inline-block";
        document.getElementById("editDetails").style.display = "none";

        saveChanges.onclick = () => saveEdit(race, fields, status, index);
        cancelChanges.onclick = () => cancelEdit(race, status, index);
    };

    const saveEdit = (race, fields, status, index) => {
        for (const key in fields) {
            race[key] = document.getElementById(`edit${key}`).value;
        }

        renderTable(status);
        raceModal.style.display = "none";
    };

    const cancelEdit = (race, status, index) => {
        openModal(status, index);
    };

    raceTable.addEventListener("click", (event) => {
        if (event.target.classList.contains("button-detail")) {
            const status = event.target.dataset.status;
            const index = event.target.dataset.index;
            openModal(status, index);
        }
    });

    closeModal.onclick = () => (raceModal.style.display = "none");

    document.getElementById("upcomingTab").onclick = () => renderTable("Upcoming");
    document.getElementById("ongoingTab").onclick = () => renderTable("On-Going");
    document.getElementById("finishedTab").onclick = () => renderTable("Finished");

    // Add edit button dynamically
    const modalButtons = document.querySelector(".modal-buttons");
    const editButton = document.createElement("button");
    editButton.id = "editDetails";
    editButton.textContent = "Edit";
    modalButtons.prepend(editButton);

    // Load default tab
    renderTable("Upcoming");
});
