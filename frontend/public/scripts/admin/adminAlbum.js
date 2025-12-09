document.addEventListener("DOMContentLoaded", () => {
    // Solo secciones del panel de ALBUMES
    const secciones = document.querySelectorAll(
        'section[data-uid][data-album-panel]'
    );
    if (!secciones.length) return;

    secciones.forEach((root) => {
        const baseCrear = root.dataset.baseCrear || "/api/admin/album";
        const baseEditar = root.dataset.baseEditar || "/api/admin/album";
        const baseEliminar = root.dataset.baseEliminar || "/api/admin/album";

        // Formularios
        const formCrear = root.querySelector('form[data-form="crear"]');
        const formEditar = root.querySelector('form[data-form="editar"]');
        const formEliminar = root.querySelector('form[data-form="eliminar"]');

        // Selects album para editar / eliminar
        const selAlbumEditar = root.querySelector("#select-album-editar");
        const selAlbumEliminar = root.querySelector("#select-album-eliminar");

        // Select artista en crear / editar
        const selArtistaCrear = root.querySelector("#select-artista-album-crear");
        const selArtistaEditar = root.querySelector("#select-artista-album-editar");

        // (Opcional) select multiple canciones en crear/editar
        const selCancionesCrear = root.querySelector(
            '[name="cancionesIds"][data-role="crear"]'
        );
        const selCancionesEditar = root.querySelector(
            '[name="cancionesIds"][data-role="editar"]'
        );

        // Mensajes
        const msgCrear = root.querySelector('[data-msg="crear"]');
        const msgEditar = root.querySelector('[data-msg="editar"]');
        const msgEliminar = root.querySelector('[data-msg="eliminar"]');

        // --------- TABS ---------
        const tabs = Array.from(root.querySelectorAll("[data-tab]"));
        const panels = Array.from(root.querySelectorAll("[data-panel]"));

        function activar(tab) {
            tabs.forEach((btn) => {
                btn.classList.toggle("active", btn.dataset.tab === tab);
            });

            panels.forEach((panel) => {
                const activo = panel.getAttribute("data-panel") === tab;
                panel.classList.toggle("hidden", !activo);
                panel.classList.toggle("block", activo);
            });

            // Select2 solo al entrar en el tab, para que no se rompa
            if (window.jQuery && $.fn.select2) {
                if (tab === "crear") {
                    if (selArtistaCrear) $(selArtistaCrear).select2();
                    if (selCancionesCrear) $(selCancionesCrear).select2();
                }
                if (tab === "editar") {
                    if (selAlbumEditar) $(selAlbumEditar).select2();
                    if (selArtistaEditar) $(selArtistaEditar).select2();
                    if (selCancionesEditar) $(selCancionesEditar).select2();
                }
                if (tab === "eliminar") {
                    if (selAlbumEliminar) $(selAlbumEliminar).select2();
                }
            }
        }

        root.addEventListener("click", (e) => {
            const btn = e.target.closest("[data-tab]");
            if (!btn) return;
            activar(btn.dataset.tab);
        });

        // Inicio en crear
        activar("crear");

        // --------- Helpers mensajes / fetch / errores ---------
        function setMsg(el, ok, texto) {
            if (!el) return;
            el.textContent = texto;
            el.style.color = ok ? "#4ade80" : "#f97373"; // verde / rojo
        }

        async function fetchAdmin(url, options = {}) {
            const token = localStorage.getItem("token");

            const headers = {
                ...(options.headers || {}),
                ...(token ? { Authorization: `Bearer ${token}` } : {}),
            };

            const res = await fetch(url, {
                ...options,
                headers,
            });

            if (!res.ok) {
                const text = await res.text().catch(() => "");
                console.error("Error backend albumes:", res.status, text);
                throw new Error(`HTTP ${res.status}`);
            }
            return res;
        }

        function clearFieldErrors(form) {
            if (!form) return;

            form.querySelectorAll("[data-error]").forEach((el) => {
                el.textContent = "";
            });

            form.querySelectorAll(".input-error").forEach((el) => {
                el.classList.remove(
                    "input-error",
                    "border-red-500",
                    "ring-2",
                    "ring-red-500/70"
                );
            });
        }

        function setFieldError(form, fieldName, message) {
            if (!form) return;

            const input = form.querySelector(`[name="${fieldName}"]`);
            const error = form.querySelector(`[data-error="${fieldName}"]`);

            if (input) {
                input.classList.add("input-error");
                input.classList.add("border-red-500", "ring-2", "ring-red-500/70");
            }

            if (error) {
                error.textContent = message;
            }
        }

        function activarLimpiezaErrores(form) {
            if (!form) return;

            form.querySelectorAll("input[name], textarea[name]").forEach((input) => {
                input.addEventListener("input", () => {
                    const field = input.name;
                    const error = form.querySelector(`[data-error="${field}"]`);

                    input.classList.remove(
                        "input-error",
                        "border-red-500",
                        "ring-2",
                        "ring-red-500/70"
                    );
                    if (error) error.textContent = "";
                });
            });

            form.querySelectorAll("select[name]").forEach((select) => {
                select.addEventListener("change", () => {
                    const field = select.name;
                    const error = form.querySelector(`[data-error="${field}"]`);

                    select.classList.remove(
                        "input-error",
                        "border-red-500",
                        "ring-2",
                        "ring-red-500/70"
                    );
                    if (error) error.textContent = "";
                });
            });
        }

        if (formCrear) activarLimpiezaErrores(formCrear);
        if (formEditar) activarLimpiezaErrores(formEditar);
        if (formEliminar) activarLimpiezaErrores(formEliminar);

        // Helper para obtener array de ids de un select multiple
        function obtenerIdsDesdeSelectMultiple(select) {
            if (!select) return [];
            return Array.from(select.options)
                .filter((opt) => opt.selected && opt.value)
                .map((opt) => Number(opt.value))
                .filter((n) => !Number.isNaN(n));
        }

        // ========= CREAR ALBUM (POST /api/admin/album) =========
        if (formCrear) {
            formCrear.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseCrear) return;

                clearFieldErrors(formCrear);

                try {
                    const raw = new FormData(formCrear);

                    const titulo = raw.get("titulo");
                    const descripcion = raw.get("descripcion") || null;
                    const artistaIdRaw = raw.get("artistaId");
                    const artistaId = artistaIdRaw ? Number(artistaIdRaw) : null;
                    const portadaFile = raw.get("portada");

                    const cancionesIds = obtenerIdsDesdeSelectMultiple(
                        selCancionesCrear
                    );

                    let hayError = false;

                    if (!titulo || !titulo.trim()) {
                        setFieldError(
                            formCrear,
                            "titulo",
                            "El titulo del album no puede estar vacio"
                        );
                        hayError = true;
                    }

                    if (!artistaId) {
                        setFieldError(
                            formCrear,
                            "artistaId",
                            "Debes seleccionar un artista"
                        );
                        hayError = true;
                    }

                    if (hayError) {
                        setMsg(msgCrear, false, "Revisa los campos marcados en rojo");
                        return;
                    }

                    const dto = {
                        titulo: titulo.trim(),
                        descripcion: descripcion,
                        portadaUrl: null, // la rellena el backend
                        artistaId: artistaId,
                        cancionesIds: cancionesIds,
                    };

                    const fd = new FormData();
                    fd.append("datos", JSON.stringify(dto));

                    if (portadaFile instanceof File && portadaFile.size > 0) {
                        fd.append("portada", portadaFile);
                    }

                    await fetchAdmin(baseCrear, {
                        method: "POST",
                        body: fd,
                    });

                    setMsg(msgCrear, true, "Album creado correctamente ✅");
                    formCrear.reset();

                    // Notificar para recargar selects globales
                    document.dispatchEvent(
                        new CustomEvent("albums-actualizados")
                    );

                    // Actualizar estadísticas
                    window.dispatchEvent(new Event("estadisticas:actualizar"));


                } catch (err) {
                    if (String(err.message).includes("401")) {
                        setMsg(
                            msgCrear,
                            false,
                            "No autorizado. Vuelve a iniciar sesion."
                        );
                    } else {
                        setMsg(
                            msgCrear,
                            false,
                            "Error al crear el album ❌"
                        );
                    }
                }
            });
        }

        // ========= EDITAR ALBUM (PUT /api/admin/album/{id}) =========
        if (formEditar && selAlbumEditar) {
            formEditar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseEditar) return;

                clearFieldErrors(formEditar);

                try {
                    const albumId = selAlbumEditar.value;
                    const raw = new FormData(formEditar);

                    //const titulo = raw.get("titulo")
                    //const descripcion = raw.get("descripcion")

                    // Limpiamos valor escrito en el input
                    let titulo =
                        typeof tituloRaw === "string" ? tituloRaw.trim() : "";

                    // Si el input está vacio, usamos el texto del <option> seleccionado
                    if (!titulo && selAlbumEditar.selectedIndex >= 0) {
                        const opt =
                            selAlbumEditar.options[selAlbumEditar.selectedIndex];
                        if (opt && typeof opt.textContent === "string") {
                            titulo = opt.textContent.trim();
                        }
                    }

                    // 3) Descripcion puede ser opcional
                    const descripcion =
                        typeof descripcionRaw === "string" &&
                            descripcionRaw.trim() !== ""
                            ? descripcionRaw.trim()
                            : null;

                    const artistaIdRaw = raw.get("artistaId");
                    const artistaId = artistaIdRaw ? Number(artistaIdRaw) : null;

                    const cancionesIds = obtenerIdsDesdeSelectMultiple(
                        selCancionesEditar
                    );

                    let hayError = false;

                    if (!albumId) {
                        setFieldError(
                            formEditar,
                            "albumId",
                            "Debes seleccionar un album"
                        );
                        hayError = true;
                    }

                    if (!artistaId) {
                        setFieldError(
                            formEditar,
                            "artistaId",
                            "Debes seleccionar un artista"
                        );
                        hayError = true;
                    }

                    if (hayError) {
                        setMsg(msgEditar, false, "Revisa los campos marcados en rojo");
                        return;
                    }

                    // El PUT del backend espera JSON (AlbumRequestDTO)
                    const dto = {
                        titulo: titulo,
                        descripcion: descripcion,
                        // portadaUrl no se toca aqui (se mantiene la que tiene)
                        portadaUrl: null,
                        artistaId: artistaId,
                        cancionesIds: cancionesIds,
                    };



                    await fetchAdmin(`${baseEditar}/${albumId}`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(dto),
                    });

                    setMsg(msgEditar, true, "Album editado correctamente ✅");

                    document.dispatchEvent(
                        new CustomEvent("albums-actualizados")
                    );

                } catch (err) {
                    if (String(err.message).includes("401")) {
                        setMsg(
                            msgEditar,
                            false,
                            "No autorizado. Vuelve a iniciar sesion."
                        );
                    } else {
                        setMsg(
                            msgEditar,
                            false,
                            "Error al editar el album ❌"
                        );
                    }
                }
            });
        }

        // ========= ELIMINAR ALBUM (DELETE /api/admin/album/{id}) =========
        if (formEliminar && selAlbumEliminar) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseEliminar) return;

                if (msgEliminar) {
                    msgEliminar.textContent = "";
                    msgEliminar.style.color = "";
                    msgEliminar.classList.remove("text-red-400");
                }

                clearFieldErrors(formEliminar);

                const id = selAlbumEliminar.value;
                let hayError = false;

                if (!id) {
                    setFieldError(formEliminar, "eliminar", "Selecciona un album");
                    hayError = true;
                }

                if (hayError) {
                    setMsg(msgEliminar, false, "Revisa los campos marcados en rojo");
                    return;
                }

                try {
                    await fetchAdmin(`${baseEliminar}/${id}`, {
                        method: "DELETE",
                    });

                    setMsg(
                        msgEliminar,
                        true,
                        "Album eliminado correctamente ✅"
                    );

                    selAlbumEliminar.value = "";
                    if (window.jQuery && $.fn.select2) {
                        $(selAlbumEliminar).trigger("change");
                    }

                    document.dispatchEvent(
                        new CustomEvent("albums-actualizados")
                    );

                    // Actualizar estadísticas
                    window.dispatchEvent(new Event("estadisticas:actualizar"));


                } catch (err) {
                    if (String(err.message).includes("401")) {
                        setMsg(
                            msgEliminar,
                            false,
                            "No autorizado. Vuelve a iniciar sesion."
                        );
                    } else {
                        setMsg(
                            msgEliminar,
                            false,
                            "Error al eliminar el album ❌"
                        );
                    }
                }
            });
        }
    });
});
