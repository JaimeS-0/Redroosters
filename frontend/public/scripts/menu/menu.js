document.addEventListener("DOMContentLoaded", () => {
    const btnOpen = document.getElementById("menuBtn");
    const btnClose = document.getElementById("closeMenu");
    const sideMenu = document.getElementById("sideMenu");
    const fondoNegro = document.getElementById("menuFondoNegro");

    const openMenu = () => {
        // Abrir menu
        sideMenu.classList.remove("translate-x-full");
        fondoNegro.classList.remove("pointer-events-none", "opacity-0");
        fondoNegro.classList.add("opacity-100");
        //document.body.classList.add("overflow-hidden");

        // Icono hamburguesa -> X
        btnOpen.classList.add("menu-open");
        btnOpen.setAttribute("aria-expanded", "true");
    };

    const closeMenu = () => {
        // Cerrar menu
        sideMenu.classList.add("translate-x-full");
        fondoNegro.classList.add("pointer-events-none", "opacity-0");
        fondoNegro.classList.remove("opacity-100");
        document.body.classList.remove("overflow-hidden");

        // Icono X -> hamburguesa
        btnOpen.classList.remove("menu-open");
        btnOpen.setAttribute("aria-expanded", "false");
    };

    btnOpen.addEventListener("click", () => {
        if (btnOpen.getAttribute("aria-expanded") === "true") {
            closeMenu();
        } else {
            openMenu();
        }
    });

    btnClose?.addEventListener("click", closeMenu);
    fondoNegro?.addEventListener("click", closeMenu);

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && btnOpen.getAttribute("aria-expanded") === "true") {
            closeMenu();
        }
    });
});
