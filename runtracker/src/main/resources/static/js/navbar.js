document.addEventListener("DOMContentLoaded", function () {
    // Mengecek path URL untuk memastikan berada di halaman yang benar
    if (window.location.pathname.includes("/home")) {
        // Mendapatkan elemen-elemen modal dan tombol
        const modal = document.getElementById("about-us-modal");
        const openButton = document.getElementById("about-us-button");
        const closeButton = document.querySelector(".close-button");

        if (openButton && modal && closeButton) {
            // Menambahkan event listener untuk membuka modal
            openButton.addEventListener("click", function (e) {
                e.preventDefault();
                modal.style.display = "flex"; // Pastikan modal ditampilkan
            });

            // Menambahkan event listener untuk menutup modal
            closeButton.addEventListener("click", function () {
                modal.style.display = "none"; // Menyembunyikan modal
            });

            // Menutup modal jika klik di luar modal
            window.addEventListener("click", function (e) {
                if (e.target === modal) {
                    modal.style.display = "none"; // Menyembunyikan modal
                }
            });
        }
    }
});
