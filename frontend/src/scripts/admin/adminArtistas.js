document.addEventListener("DOMContentLoaded", () => {

    const secciones = document.querySelectorAll("section[data-uid]");
    console.log("secciones encontradas:", secciones.length);

    if (!secciones.length) return;

    secciones.forEach((root) => {
        const tabs = Array.from(root.querySelectorAll("[data-tab]"));
        const panels = Array.from(root.querySelectorAll("[data-panel]"));

        function pintaBotones(activo) {
            tabs.forEach((btn) => btn.classList.remove("active"));
            const actual = root.querySelector(`[data-tab="${activo}"]`);
            if (actual) actual.classList.add("active");
        }

        function pintaPaneles(activo) {
            panels.forEach((p) => {
                const esActivo = p.getAttribute("data-panel") === activo;
                p.classList.toggle("hidden", !esActivo);
            });
        }

        function activar(tab) {
            pintaBotones(tab);
            pintaPaneles(tab);
        }

        root.addEventListener("click", (e) => {
            const btn = e.target.closest("[data-tab]");
            if (!btn) return;
            activar(btn.dataset.tab);
        });

        // Inicio
        activar("crear");

        console.log("Funcionaaaaaaaaaaaa");
    });


});
