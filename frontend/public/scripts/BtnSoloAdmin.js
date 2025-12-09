document.addEventListener("DOMContentLoaded", () => {

    const userRaw = localStorage.getItem("rr_user");
    let role = null;

    if (userRaw) {
        try {
            const user = JSON.parse(userRaw);
            role = user.role;
        } catch { }
    }

    const adminButtons = document.querySelectorAll("[data-admin-only]");

    if (role === "ADMIN") {
        // Mostrar botones
        adminButtons.forEach(btn => btn.classList.remove("hidden"));
    } else {
        // Ocultarlos si NO es admin
        adminButtons.forEach(btn => btn.style.display = "none");
    }
});
