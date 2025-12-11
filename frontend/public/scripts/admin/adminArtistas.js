document.addEventListener("DOMContentLoaded", () => {
    const secciones = document.querySelectorAll(
        'section[data-uid][data-base][data-base-public]'
    );
    if (!secciones.length) return;

    secciones.forEach((root) => {
        const baseAdmin = root.dataset.base;

        const formCrear = root.querySelector('form[data-form="crear"]');
        const formEditar = root.querySelector('form[data-form="editar"]');
        const formEliminar = root.querySelector('form[data-form="eliminar"]');

        if (formCrear) activarLimpiezaErrores(formCrear);
        if (formEditar) activarLimpiezaErrores(formEditar);
        if (formEliminar) activarLimpiezaErrores(formEliminar);

        const selArtistaEditar = root.querySelector("#select-artista-editar");
        const selArtistaEliminar = root.querySelector("#select-artista-eliminar");

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

            // Inicializamos select2 de artistas
            if (window.jQuery && $.fn.select2) {
                if (tab === "editar") {
                    if (selArtistaEditar) $(selArtistaEditar).select2();
                }
                if (tab === "eliminar") {
                    if (selArtistaEliminar) $(selArtistaEliminar).select2();
                }
            }
        }

        root.addEventListener("click", (e) => {
            const btn = e.target.closest("[data-tab]");
            if (!btn) return;
            activar(btn.dataset.tab);
        });

        // Empezamos en CREAR
        activar("crear");

        // Helpers mensajes / fetch 
        function setMsg(el, ok, texto) {
            if (!el) return;
            el.textContent = texto;
            el.style.color = ok ? "#4ade80" : "#f97373";
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
                console.error("Error backend artistas:", res.status, text);
                throw new Error(`HTTP ${res.status}`);
            }
            return res;
        }

        // Errores 
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
                input.classList.add(
                    "input-error",
                    "border-red-500",
                    "ring-2",
                    "ring-red-500/70"
                );
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


        // Helper para resetear selects (normal o select2)
        function resetSelect(select, isMultiple = false) {
            if (!select) return;

            if (window.jQuery && $.fn.select2) {
                $(select).val(isMultiple ? null : "").trigger("change");
            } else {
                select.value = "";
            }
        }


        // ========= CREAR ARTISTA (POST /api/admin/artista) =========
        if (formCrear) {
            formCrear.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseAdmin) return;

                clearFieldErrors(formCrear);

                try {
                    const raw = new FormData(formCrear);

                    const nombre = raw.get("nombre");
                    const descripcion = raw.get("descripcion") || null;
                    const destacadoChecked = raw.get("destacado") === "on";
                    const portadaFile = raw.get("portada");

                    let hayError = false;

                    if (!nombre || !nombre.trim()) {
                        setFieldError(
                            formCrear,
                            "nombre",
                            "El artista no puede estar vacio"
                        );
                        hayError = true;
                    }

                    if (hayError) {
                        setMsg(msgCrear, false, "Revisa los campos marcados en rojo");
                        return;
                    }

                    const fd = new FormData();
                    fd.append("nombre", nombre.trim());
                    if (descripcion && descripcion.trim() !== "") {
                        fd.append("descripcion", descripcion.trim());
                    }
                    fd.append("destacado", String(destacadoChecked));
                    if (portadaFile instanceof File && portadaFile.size > 0) {
                        fd.append("portada", portadaFile);
                    }

                    await fetchAdmin(baseAdmin, {
                        method: "POST",
                        body: fd,
                    });

                    setMsg(msgCrear, true, "Artista creado correctamente ✅");
                    formCrear.reset();

                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));
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
                            "Error al crear el artista ❌"
                        );
                    }
                }
            });
        }

        // ========= EDITAR ARTISTA (PUT /api/admin/artista/{id}) =========
        if (formEditar && selArtistaEditar) {
            formEditar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseAdmin) return;

                clearFieldErrors(formEditar);

                try {
                    const raw = new FormData(formEditar);

                    const id = selArtistaEditar.value;

                    const nombre = raw.get("nombre") || null;
                    const descripcion = raw.get("descripcion") || null;
                    const destacadoChecked = raw.get("destacado") === "on";
                    const portadaFile = raw.get("portada");

                    let hayError = false;

                    if (!id) {
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

                    const fd = new FormData();
                    if (nombre && nombre.trim() !== "") {
                        fd.append("nombre", nombre.trim());
                    }
                    if (descripcion && descripcion.trim() !== "") {
                        fd.append("descripcion", descripcion.trim());
                    }
                    fd.append("destacado", String(destacadoChecked));
                    if (portadaFile instanceof File && portadaFile.size > 0) {
                        fd.append("portada", portadaFile);
                    }

                    await fetchAdmin(`${baseAdmin}/${id}`, {
                        method: "PUT",
                        body: fd,
                    });

                    formEditar.reset();

                    setMsg(msgEditar, true, "Artista editado correctamente ✅");

                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));
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
                            "Error al editar el artista ❌"
                        );
                    }
                }
            });
        }

        // ========= ELIMINAR ARTISTAS (DELETE /api/admin/artista/{id}) =========
        if (formEliminar && selArtistaEliminar) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseAdmin) return;

                clearFieldErrors(formEliminar);

                const id = selArtistaEliminar.value;
                let hayError = false;

                if (!id) {
                    setFieldError(formEliminar, "eliminar", "Selecciona un Artista");
                    hayError = true;
                }

                if (hayError) {
                    setMsg(msgEliminar, false, "Revisa los campos marcados en rojo");
                    return;
                }

                try {
                    await fetchAdmin(`${baseAdmin}/${id}`, {
                        method: "DELETE",
                    });

                    setMsg(
                        msgEliminar,
                        true,
                        "Artista eliminado correctamente ✅"
                    );

                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));
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
                            "Error al eliminar el artista ❌"
                        );
                    }
                }
            });
        }
    });
});
