document.addEventListener("DOMContentLoaded", () => {
    const secciones = document.querySelectorAll("section[data-uid]");
    console.log("secciones encontradas:", secciones.length);
    if (!secciones.length) return;

    secciones.forEach((root) => {
        const base = root.dataset.base; // "/api/admin/artistas"

        // 🔹 selects para EDITAR / ELIMINAR
        const selectArtistaEditar = root.querySelector("#select-artista-editar");
        const selectArtistaEliminar = root.querySelector("#select-artista-eliminar");

        // TABS 
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

        activar("crear");

        // INIT SELECT2 + CARGAR OPCIONES 
        const selects = root.querySelectorAll(".select2");
        if (window.jQuery && $.fn.select2) {
            selects.forEach((s) => $(s).select2());
        } else {
            console.error("jQuery o select2 no están cargados todavía");
        }

        if (typeof cargarArtistas === "function") {
            cargarArtistas(root).catch(console.error);
        } else {
            console.error("cargarArtistas no está definida");
        }

        // CREAR 
        const formCrear = root.querySelector('form[data-form="crear"]');
        const msgCrear = root.querySelector('[data-msg="crear"]');

        const fileInputCrear = formCrear?.querySelector('input[name="portada"]');
        const previewCrear = formCrear?.querySelector('[data-preview-portada]');

        if (fileInputCrear && previewCrear) {
            fileInputCrear.addEventListener("change", () => {
                if (fileInputCrear.files.length > 0) {
                    const file = fileInputCrear.files[0];
                    previewCrear.textContent = "✔ Archivo seleccionado: " + file.name;
                } else {
                    previewCrear.textContent = "Ningún archivo seleccionado";
                }
            });
        }

        if (formCrear && msgCrear && base) {
            formCrear.addEventListener("submit", async (e) => {
                e.preventDefault();
                msgCrear.textContent = "Creando artista...";

                const fd = new FormData();
                fd.append("nombre", formCrear.nombre.value.trim());
                fd.append("descripcion", formCrear.descripcion.value.trim());
                fd.append("destacado", formCrear.destacado.checked ? "true" : "false");

                if (fileInputCrear && fileInputCrear.files.length > 0) {
                    fd.append("portada", fileInputCrear.files[0]);
                }

                const token = localStorage.getItem("token");
                if (!token) {
                    msgCrear.textContent = "No hay sesion iniciada.";
                    msgCrear.className = "text-red-500 text-center mt-2";
                    return;
                }

                const url = window.location.origin + base;
                console.log("URL FINAL QUE SE LLAMA (CREAR):", url);

                try {
                    const res = await fetch(url, {
                        method: "POST",
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                        body: fd,
                    });

                    let body = {};
                    try { body = await res.json(); } catch (_) { }

                    if (res.ok) {
                        msgCrear.textContent = "Artista creado correctamente.";
                        msgCrear.className = "text-green-500 text-center mt-4";
                        formCrear.reset();
                        if (previewCrear) previewCrear.textContent = "Ningún archivo seleccionado";

                        // 🔁 refrescamos selects sin recargar
                        if (typeof cargarArtistas === "function") {
                            await cargarArtistas(root);
                        }
                    } else {
                        msgCrear.textContent = body.message || "Error al crear artista.";
                        msgCrear.className = "text-red-500 text-center mt-2";
                    }
                } catch (err) {
                    console.error(err);
                    msgCrear.textContent = "Error de red al crear el artista.";
                    msgCrear.className = "text-red-500 text-center mt-2";
                }
            });
        }

        // EDITAR 
        const formEditar = root.querySelector('form[data-form="editar"]');
        const msgEditar = root.querySelector('[data-msg="editar"]');

        if (formEditar && msgEditar && base && selectArtistaEditar) {
            const fileInputEditar = formEditar.querySelector('input[name="portada"]');
            const previewEditar = formEditar.querySelector('[data-preview-portada-editar]');

            if (fileInputEditar && previewEditar) {
                previewEditar.textContent = "Ningún archivo seleccionado";
                fileInputEditar.addEventListener("change", () => {
                    if (fileInputEditar.files.length > 0) {
                        previewEditar.textContent =
                            "✔ Archivo seleccionado: " + fileInputEditar.files[0].name;
                    } else {
                        previewEditar.textContent = "Ningún archivo seleccionado";
                    }
                });
            }

            formEditar.addEventListener("submit", async (e) => {
                e.preventDefault();
                msgEditar.textContent = "Guardando cambios...";

                const id = selectArtistaEditar.value?.trim();
                if (!id) {
                    msgEditar.textContent = "Selecciona un artista primero.";
                    return;
                }

                const fd = new FormData();

                if (formEditar.nombre.value.trim() !== "") {
                    fd.append("nombre", formEditar.nombre.value.trim());
                }
                if (formEditar.descripcion.value.trim() !== "") {
                    fd.append("descripcion", formEditar.descripcion.value.trim());
                }

                fd.append("destacado", formEditar.destacado.checked ? "true" : "false");

                if (fileInputEditar && fileInputEditar.files.length > 0) {
                    fd.append("portada", fileInputEditar.files[0]);
                }

                const token = localStorage.getItem("token");
                if (!token) {
                    msgEditar.textContent = "No hay sesion iniciada.";
                    msgEditar.className = "text-red-500 text-center mt-2";
                    return;
                }

                const url = window.location.origin + base + "/" + encodeURIComponent(id);
                console.log("URL FINAL QUE SE LLAMA (EDITAR):", url);

                try {
                    const res = await fetch(url, {
                        method: "PUT",
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                        body: fd,
                    });

                    let body = {};
                    try { body = await res.json(); } catch (_) { }

                    if (res.ok) {
                        msgEditar.textContent = "Artista actualizado correctamente.";
                        msgEditar.className = "text-green-500 text-center mt-4";

                        // refrescar selects
                        if (typeof cargarArtistas === "function") {
                            await cargarArtistas(root);
                        }
                    } else {
                        msgEditar.textContent =
                            body.message || "Error al actualizar artista.";
                        msgEditar.className = "text-red-500 text-center mt-2";
                    }
                } catch (err) {
                    console.error(err);
                    msgEditar.textContent = "Error de red al actualizar el artista.";
                    msgEditar.className = "text-red-500 text-center mt-2";
                }
            });
        }

        //  ELIMINAR 
        const formEliminar = root.querySelector('form[data-form="eliminar"]');
        const msgEliminar = root.querySelector('[data-msg="eliminar"]');

        if (formEliminar && msgEliminar && base && selectArtistaEliminar) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();

                msgEliminar.textContent = "Eliminando artista...";

                const id = selectArtistaEliminar.value?.trim();
                if (!id) {
                    msgEliminar.textContent = "Selecciona un artista primero.";
                    return;
                }

                const token = localStorage.getItem("token");
                if (!token) {
                    msgEliminar.textContent = "No hay sesion iniciada.";
                    msgEliminar.className = "text-red-500 text-center mt-2";
                    return;
                }

                const url = `${window.location.origin + base}/${encodeURIComponent(id)}`;
                console.log("URL DELETE:", url);

                try {
                    const res = await fetch(url, {
                        method: "DELETE",
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                    });

                    if (res.ok || res.status === 204) {
                        msgEliminar.textContent = "Artista eliminado correctamente.";
                        msgEliminar.className = "text-green-500 text-center mt-2";

                        if (typeof cargarArtistas === "function") {
                            await cargarArtistas(root);
                        }

                        if (window.jQuery && $.fn.select2) {
                            $(selectArtistaEliminar).val("").trigger("change");
                        } else {
                            selectArtistaEliminar.value = "";
                        }
                    } else {
                        let body = {};
                        try { body = await res.json(); } catch (_) { }

                        msgEliminar.textContent =
                            body.message || "Error al eliminar el artista.";
                        msgEliminar.className = "text-red-500 text-center mt-2";
                    }
                } catch (err) {
                    console.error(err);
                    msgEliminar.textContent =
                        "Error de red al eliminar el artista.";
                    msgEliminar.className = "text-red-500 text-center mt-2";
                }
            });
        }
    });
});
