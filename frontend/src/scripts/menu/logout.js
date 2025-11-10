document.addEventListener("DOMContentLoaded", () => {
    const logoutLink = document.getElementById("logoutLink");
    const modal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelLogout");
    const confirmBtn = document.getElementById("confirmLogout");
    const closeBtn = document.getElementById("closeLogoutModal");
    const titleEl = document.getElementById("logoutModalTitle");
    const textEl = document.getElementById("logoutModalText");

    if (!logoutLink || !modal) return;

    const openModal = () => {
        modal.classList.remove("hidden");
        modal.classList.add("flex");
        document.body.classList.add("overflow-hidden");
    };

    const closeModal = () => {
        modal.classList.remove("flex");
        modal.classList.add("hidden");
        document.body.classList.remove("overflow-hidden");
    };

    closeBtn?.addEventListener("click", closeModal);

    logoutLink.addEventListener("click", (e) => {
        e.preventDefault();

        // ¿Hay sesión?
        const tokenMatch = document.cookie.match(/rr_token=([^;]+)/);
        const hasSession = !!tokenMatch;

        if (hasSession) {
            // Si hay sesión -> modal de cerrar sesión
            if (titleEl)
                titleEl.textContent = "¿Cerrar sesión?";
            if (textEl)
                textEl.textContent =
                    "Tu sesión se cerrará y volverás a la página de inicio.";

            // Mostramos botón de confirmar
            if (confirmBtn) {
                confirmBtn.classList.remove("hidden");
                confirmBtn.onclick = () => {
                    document.cookie = "rr_token=; path=/; max-age=0";
                    localStorage.removeItem("rr_user");
                    window.location.href = "/login";
                };
            }

            // cierra modal
            if (cancelBtn) {
                cancelBtn.textContent = "Cancelar";
                cancelBtn.onclick = closeModal;
            }
        } else {
            // NO hay sesión -> añadimos el mensaje 
            if (titleEl)
                titleEl.textContent = "No has iniciado sesión";
            if (textEl)
                textEl.textContent =
                    "Debes iniciar sesión para poder cerrar tu cuenta.";

            // Ocultamos botón de confirmar
            if (confirmBtn) {
                confirmBtn.classList.add("hidden");
                confirmBtn.onclick = null;
            }

            // El botón de "Cancelar" ahora lleva al login
            if (cancelBtn) {
                cancelBtn.textContent = "Ir a iniciar sesión";
                cancelBtn.onclick = () => {
                    window.location.href = "/login";
                };
            }
        }

        openModal();
    });
});
