document.addEventListener("DOMContentLoaded", () => {
    // Data Dummy
    const totalMembers = 150; // Total member terdaftar
    const completedRaces = 25; // Total race yang sudah dilaksanakan
    const upcomingRaces = 10; // Total race yang akan dilaksanakan
    const raceData = {
        2025: [
            { name: "Race 1", members: 50 },
            { name: "Race 2", members: 30 },
            { name: "Race 3", members: 70 },
        ],
        2024: [
            { name: "Race 1", members: 45 },
            { name: "Race 2", members: 20 },
            { name: "Race 3", members: 60 },
        ],
    };

    // Update Stats
    document.getElementById("totalMembers").textContent = totalMembers;
    document.getElementById("completedRaces").textContent = completedRaces;
    document.getElementById("upcomingRaces").textContent = upcomingRaces;

    // Populate Year Selector
    const yearSelector = document.getElementById("yearSelector");
    const currentYear = new Date().getFullYear();
    Object.keys(raceData).forEach((year) => {
        const option = document.createElement("option");
        option.value = year;
        option.textContent = year;
        if (parseInt(year) === currentYear) option.selected = true;
        yearSelector.appendChild(option);
    });

    // Chart
    const ctx = document.getElementById("raceChart").getContext("2d");
    const chartData = generateChartData(raceData[currentYear]);

    const raceChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: chartData.labels, // Nama race
            datasets: [
                {
                    label: "Jumlah Member",
                    data: chartData.data, // Jumlah member per race
                    backgroundColor: "rgba(255, 99, 132, 0.5)",
                    borderColor: "rgba(255, 99, 132, 1)",
                    borderWidth: 1,
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
            },
            scales: {
                x: { title: { display: true, text: "Nama Race" } },
                y: { title: { display: true, text: "Jumlah Member" } },
            },
        },
    });

    // Update Chart on Year Change
    yearSelector.addEventListener("change", () => {
        const selectedYear = yearSelector.value;
        const updatedData = generateChartData(raceData[selectedYear]);
        raceChart.data.labels = updatedData.labels;
        raceChart.data.datasets[0].data = updatedData.data;
        raceChart.update();
    });

    // Helper: Generate Chart Data
    function generateChartData(data) {
        const labels = [];
        const members = [];
        data.forEach((race) => {
            labels.push(race.name);
            members.push(race.members);
        });
        return { labels, data: members };
    }
});
