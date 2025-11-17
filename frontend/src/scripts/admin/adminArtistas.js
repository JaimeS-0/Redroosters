// src/scripts/admin/adminArtistas.js
document.addEventListener("DOMContentLoaded", () => {
    // Buscamos todas las secciones que tengan el data-uid
    const secciones = document.querySelectorAll("section[data-uid]");
    console.log("secciones encontradas:", secciones.length);

    if (!secciones.length) return;

    // Recorremos cada sección encontrada
    secciones.forEach((root) => {
        // ---- TABS (crear / editar / eliminar) ----
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

        // Panel inicial
        activar("crear");

        const base = root.dataset.base; // "/api/admin/artistas"
        

        // CREAR ARTISTA (POST)
        const formCrear = root.querySelector('form[data-form="crear"]');
        const msgCrear = root.querySelector('[data-msg="crear"]');

        // preview del archivo en CREAR
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
                    try {
                        body = await res.json();
                    } catch (_) { }

                    if (res.ok) {
                        msgCrear.textContent = "Artista creado correctamente.";
                        msgCrear.className = "text-green-500 text-center mt-4";
                        formCrear.reset();
                        if (previewCrear) previewCrear.textContent = "Ningún archivo seleccionado";
                    } else {
                        msgCrear.textContent =
                            body.message || "Error al crear artista.";
                        msgCrear.className = "text-red-500 text-center mt-2";

                    }
                } catch (err) {
                    console.error(err);
                    msgCrear.textContent = "Error de red al crear el artista.";
                    msgCrear.className = "text-red-500 text-center mt-2";

                }
            });
        }

        // EDITAR ARTISTA (PUT)
        const formEditar = root.querySelector('form[data-form="editar"]');
        const msgEditar = root.querySelector('[data-msg="editar"]');

        if (formEditar && msgEditar && base) {
            const fileInputEditar = formEditar.querySelector('input[name="portada"]');
            const previewEditar = formEditar.querySelector('[data-preview-portada-editar]');

            // preview del archivo en EDITAR
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

                const id = formEditar.id.value.trim();
                if (!id) {
                    msgEditar.textContent = "Falta el ID del artista.";
                    return;
                }

                const fd = new FormData();

                // los añadimos si vienen con valor
                if (formEditar.nombre.value.trim() !== "") {
                    fd.append("nombre", formEditar.nombre.value.trim());
                }
                if (formEditar.descripcion.value.trim() !== "") {
                    fd.append("descripcion", formEditar.descripcion.value.trim());
                }

                fd.append(
                    "destacado",
                    formEditar.destacado.checked ? "true" : "false"
                );

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
                    try {
                        body = await res.json();
                    } catch (_) { }

                    if (res.ok) {
                        msgEditar.textContent = "Artista actualizado correctamente.";
                        msgEditar.className = "text-green-500 text-center mt-4";
                        // formEditar.reset();  limpiar el form
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
        // ELIMINAR ARTISTA DELETE
        const formEliminar = root.querySelector('form[data-form="eliminar"]');
        const msgEliminar = root.querySelector('[data-msg="eliminar"]');

        if (formEliminar && msgEliminar && base) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();

                msgEliminar.textContent = "Eliminando artista...";

                const id = formEliminar.id.value.trim();
                if (!id) {
                    msgEliminar.textContent = "Falta el ID del artista.";
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
                            "Authorization": "Bearer " + token,
                        },
                    });

                    if (res.ok || res.status === 204) {
                        msgEliminar.textContent = "Artista eliminado correctamente.";
                        msgEliminar.className =
                            "text-green-500 text-center mt-2";
                        formEliminar.reset();
                    } else {
                        let body = {};
                        try {
                            body = await res.json();
                        } catch (_) { }

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
