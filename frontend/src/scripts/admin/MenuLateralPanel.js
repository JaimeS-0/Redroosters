document.addEventListener("DOMContentLoaded", () => {

    const btns = Array.from(document.querySelectorAll(".sidebar-btn"));
    const views = Array.from(
        document.querySelectorAll('#contenido div[id^="vista-"]'),
    );

    function setActive(id) {
        // Quita la clase active a todos los botones
        btns.forEach((b) => b.classList.remove("active"));

        // Busca el boton con data-target concide con el id que selecionamos
        const current = btns.find(
            (b) => b.getAttribute("data-target") === id,
        );

        // Solo enseñamos la vista selecionada y las demas ocultas
        if (current) current.classList.add("active");
        views.forEach((v) => v.classList.toggle("hidden", v.id !== id));
    }

    btns.forEach((b) =>
        b.addEventListener("click", () =>
            setActive(b.getAttribute("data-target")),
        ),
    );

    const style = document.createElement("style");
    style.textContent = `.sidebar-btn.active{background:rgba(255,255,255,.08)}`;
    document.head.appendChild(style);

})

