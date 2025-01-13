document.addEventListener("DOMContentLoaded", () => {
    // Data Dummy
    const totalDistance = 120; // Total jarak (km)
    const totalActivities = 15; // Total aktivitas
    const totalDuration = 40; // Total durasi (jam)
    const totalRaces = 4; // Total race

    const distancePerDay = {
        "2025-01-01": 5,
        "2025-01-02": 7,
        "2025-01-03": 10,
        "2025-01-05": 8,
    };

    // Update Stats
    document.getElementById("totalDistance").textContent = `${totalDistance} km`;
    document.getElementById("totalActivities").textContent = totalActivities;
    document.getElementById("totalDuration").textContent = `${totalDuration} jam`;
    document.getElementById("totalRaces").textContent = totalRaces;

    // Populate Month Selector
    const monthSelector = document.getElementById("monthSelector");
    const currentMonth = new Date().toISOString().slice(0, 7); // Default: bulan ini
    monthSelector.innerHTML = `
        <option value="2025-01" selected>Januari 2025</option>
        <option value="2025-02">Februari 2025</option>
        <option value="2025-03">Maret 2025</option>
    `;

    // Chart
    const ctx = document.getElementById("distanceChart").getContext("2d");
    const chartData = generateChartData(distancePerDay);

    const distanceChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: chartData.labels, // Tanggal
            datasets: [
                {
                    label: "Jarak (km)",
                    data: chartData.data, // Jarak per hari
                    borderColor: "rgba(51, 102, 255, 1)",
                    backgroundColor: "rgba(51, 102, 255, 0.2)",
                    tension: 0.3,
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
            },
            scales: {
                x: { title: { display: true, text: "Tanggal" } },
                y: { title: { display: true, text: "Jarak (km)" } },
            },
        },
    });

    // Update Chart on Month Change
    monthSelector.addEventListener("change", () => {
        const selectedMonth = monthSelector.value;
        const updatedData = generateChartData(distancePerDay, selectedMonth);
        distanceChart.data.labels = updatedData.labels;
        distanceChart.data.datasets[0].data = updatedData.data;
        distanceChart.update();
    });

    // Helper: Generate Chart Data
    function generateChartData(data, month = currentMonth) {
        const labels = [];
        const distances = [];
        Object.keys(data).forEach((date) => {
            if (date.startsWith(month)) {
                labels.push(date);
                distances.push(data[date]);
            }
        });
        return { labels, data: distances };
    }
});
