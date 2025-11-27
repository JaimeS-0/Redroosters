document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("deleteModal");
    const titleEl = document.getElementById("deleteModalTitle");
    const textEl = document.getElementById("deleteModalText");
    const closeBtn = document.getElementById("deleteClose");
    const cancelBtn = document.getElementById("deleteCancel");
    const confirmBtn = document.getElementById("deleteConfirm");

    if (!modal || !confirmBtn || !cancelBtn) return;

    let onConfirm = null; // callback que se ejecutara al confirmar

    const openModal = () => {
        modal.classList.remove("hidden");
        modal.classList.add("flex");
        document.body.classList.add("overflow-hidden");
    };

    const closeModal = () => {
        modal.classList.remove("flex");
        modal.classList.add("hidden");
        document.body.classList.remove("overflow-hidden");
        onConfirm = null;
    };

    closeBtn?.addEventListener("click", closeModal);
    cancelBtn.addEventListener("click", closeModal);

    modal.addEventListener("click", (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });

    confirmBtn.addEventListener("click", () => {
        if (typeof onConfirm === "function") {
            onConfirm();
        }
        closeModal();
    });

    // ---- Enganchar todos los formularios de eliminar ----
    const eliminarPanels = document.querySelectorAll('[data-panel="eliminar"]');

    eliminarPanels.forEach((panel) => {
        const tipo = panel.dataset.deleteType || "elemento"; // artista / cancion / album
        const form = panel.querySelector('form[data-form="eliminar"]');
        const select = panel.querySelector("[data-delete-select]");
        const triggerBtn = panel.querySelector("[data-delete-trigger]");
        const msgEl = panel.querySelector('[data-msg="eliminar"]');

        if (!form || !select || !triggerBtn) return;

        triggerBtn.addEventListener("click", (e) => {
            e.preventDefault();

            const option = select.options[select.selectedIndex];

            if (!option || !option.value) {
                if (msgEl) {
                    // LIMPIAR EL VERDE QUE VIENE DE UN DELETE CORRECTO
                    msgEl.style.color = "";           // ← ESTA LINEA ES LA CLAVE
                    msgEl.textContent = `Selecciona un ${tipo} primero.`;
                    msgEl.classList.remove("text-gray-300");
                    msgEl.classList.add("text-red-400");
                }
                return;
            }



            const nombre = option.textContent?.trim() || "";

            if (msgEl) {
                msgEl.textContent = "";
                msgEl.style.color = "";           // ← limpiar rojo o verde previo
                msgEl.classList.remove("text-red-400");
            }


            // Personalizamos textos del modal
            if (titleEl) titleEl.textContent = `Eliminar ${tipo}`;
            if (textEl)
                textEl.textContent = nombre
                    ? `¿Seguro que quieres eliminar "${nombre}"? Esta acción no se puede deshacer.`
                    : `¿Seguro que quieres eliminar el ${tipo} seleccionado? Esta acción no se puede deshacer.`;

            // Definimos qué pasa al confirmar
            onConfirm = () => {

                form.requestSubmit();
            };

            openModal();
        });
    });
});
