document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("tabsDiscografia");
    if (!container) return;

    const underline = container.querySelector(".disc-underline");
    const tabs = Array.from(container.querySelectorAll(".tab-disc"));

    if (!underline || tabs.length === 0) return;

    // Todos los bloques de contenido (canciones, albumes, singles)
    const contents = Array.from(
        document.querySelectorAll("[data-tab-content]"),
    );

    // Por defecto: primera pestaña (Canciones)
    let activeTab = tabs[0];

    function moverBarra(btn) {
        const rect = btn.getBoundingClientRect();
        const contRect = container.getBoundingClientRect();

        const left = rect.left - contRect.left;

        underline.style.left = `${left}px`;
        underline.style.width = `${rect.width}px`;

        // Colores activo / inactivo
        tabs.forEach((t) => {
            t.classList.remove("text-white");
            t.classList.add("text-white/60");
        });

        btn.classList.add("text-white");
        btn.classList.remove("text-white/60");

        activeTab = btn;
    }

    function activarTab(btn) {
        const target = btn.dataset.tab; // "canciones" | "albumes" | "singles"

        // Mostrar solo el contenido de la pestaña pulsada
        contents.forEach((c) => {
            const tabName = c.getAttribute("data-tab-content");

            if (tabName === target) {
                c.classList.remove("hidden");
            } else {
                c.classList.add("hidden");
            }
        });

        moverBarra(btn);
    }

    // --- Inicial ---
    const oldTransition = underline.style.transition;
    underline.style.transition = "none";

    activarTab(activeTab);

    requestAnimationFrame(() => {
        underline.style.transition = oldTransition || "";
        underline.style.opacity = "1";
    });

    // --- Eventos click ---
    tabs.forEach((tab) => {
        tab.addEventListener("click", () => activarTab(tab));
    });

    // --- Resize ---
    window.addEventListener("resize", () => moverBarra(activeTab));
});