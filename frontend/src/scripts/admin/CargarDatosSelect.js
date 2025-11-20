async function cargarArtistas(root) {
    const basePublic = root.dataset.basePublic; // "/api/public/artistas"
    const res = await fetch(basePublic);

    if (!res.ok) {
        console.error("Error al cargar artistas", res.status);
        return;
    }

    const page = await res.json();
    const artistas = page.content || [];

    const selectEditar = root.querySelector("#select-artista-editar");
    const selectEliminar = root.querySelector("#select-artista-eliminar");

    const selects = [selectEditar, selectEliminar].filter(Boolean);

    selects.forEach((select) => {
        // placeholder
        select.innerHTML = `<option value="">Selecciona un artista</option>`;

        artistas.forEach((a) => {
            const opt = document.createElement("option");
            opt.value = a.id;
            opt.textContent = a.nombre;
            select.appendChild(opt);
        });

        // refrescar select2
        if (window.jQuery && $.fn.select2) {
            $(select).trigger("change");
        }
    });
}
