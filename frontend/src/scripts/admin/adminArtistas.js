document.addEventListener("DOMContentLoaded", () => {
    const secciones = document.querySelectorAll(
        'section[data-uid][data-base][data-base-public]'
    );
    if (!secciones.length) return;

    secciones.forEach((root) => {
        const baseAdmin = root.dataset.base;
        const basePublic = root.dataset.basePublic;

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

            if (window.jQuery && $.fn.select2) {
                if (tab === "editar" && selArtistaEditar) {
                    $(selArtistaEditar).select2({ width: "100%" });
                }
                if (tab === "eliminar" && selArtistaEliminar) {
                    $(selArtistaEliminar).select2({ width: "100%" });
                }
            }
        }

        root.addEventListener("click", (e) => {
            const btn = e.target.closest("[data-tab]");
            if (!btn) return;
            activar(btn.dataset.tab);
        });

        activar("crear");

        // --------- Mensajes ---------
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

            //console.log("Respuesta backend:", res.status, url);

            if (!res.ok) {
                const text = await res.text().catch(() => "");
                //console.log("Body error backend:", text);
                throw new Error(`HTTP ${res.status}`);
            }
            return res;
        }

        // --------------Errores-------------

        // Limpia todos los errores de campos dentro de un formulario
        function clearFieldErrors(form) {
            if (!form) return;

            // Vaciar los <p data-error="...">
            form.querySelectorAll("[data-error]").forEach((el) => {
                el.textContent = "";
            });

            // Quitar estilos rojos de los inputs
            form.querySelectorAll(".input-error").forEach((el) => {
                el.classList.remove("input-error");
                el.classList.remove("border-red-500");
                el.classList.remove("ring-2");
                el.classList.remove("ring-red-500/70");
            });

        }

        // Poner error a un campo concreto
        function setFieldError(form, fieldName, message) {
            if (!form) return;

            const input = form.querySelector(`[name="${fieldName}"]`);
            const error = form.querySelector(`[data-error="${fieldName}"]`);

            if (input) {
                input.classList.add("input-error");
                input.classList.add("border-red-500");
                input.classList.add("ring-2");
                input.classList.add("ring-red-500/70");
            }

            if (error) {
                error.textContent = message;
            }
        }

        function activarLimpiezaErrores(form) {
            if (!form) return;

            // Para inputs de texto
            form.querySelectorAll("input[name]").forEach((input) => {
                input.addEventListener("input", () => {
                    const field = input.name;
                    const error = form.querySelector(`[data-error="${field}"]`);

                    input.classList.remove("input-error", "border-red-500", "ring-2", "ring-red-500/70");
                    if (error) error.textContent = "";
                });


            });
        }

        // --------- CREAR ---------
        if (formCrear) {
            formCrear.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseAdmin) return;

                try {
                    const raw = new FormData(formCrear);

                    const nombre = raw.get("nombre");
                    const descripcion = raw.get("descripcion") || null;
                    const destacadoChecked = raw.get("destacado") === "on";
                    const portadaFile = raw.get("portada");

                    clearFieldErrors(formCrear);
                    let hayError = false

                    if (!nombre || !nombre.trim()) {
                        setFieldError(formCrear, "nombre", "El artista no puede estar vacio");
                        hayError = true;
                    }

                    // Si hay errores, no llamamos al backend
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

                    // Avisamos globalmente
                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));

                    // Actualizar estadísticas
                    window.dispatchEvent(new Event("estadisticas:actualizar"));


                } catch (err) {
                    //console.error("Error creando artista", err);
                    if (String(err.message).includes("401")) {
                        setMsg(msgCrear, false, "No autorizado. Vuelve a iniciar sesion.");
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

        // --------- EDITAR ---------
        if (formEditar && selArtistaEditar) {
            formEditar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseAdmin) return;

                try {
                    const raw = new FormData(formEditar);

                    const id = selArtistaEditar.value; // <- ID de la cancion

                    const nombre = raw.get("nombre") || null;
                    const descripcion = raw.get("descripcion") || null;
                    const destacadoChecked = raw.get("destacado") === "on";
                    const portadaFile = raw.get("portada");


                    clearFieldErrors(formEditar);
                    let hayError = false

                    if (!id) {
                        setFieldError(formEditar, "artistaId", "Debes seleccionar un artista");
                        hayError = true;
                    }

                    // Si hay errores, no llamamos al backend
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

                    setMsg(msgEditar, true, "Artista editado correctamente ✅");

                    // Avisamos globalmente
                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));

                } catch (err) {
                    //console.error("Error editando artista", err);
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

        // --------- ELIMINAR ---------
        if (formEliminar && selArtistaEliminar) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();

                //console.log("[adminArtistas] submit eliminar artista");


                if (!baseAdmin) {
                    return;
                }

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

                    // Avisamos globalmente
                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));

                    // Actualizar estadísticas
                    window.dispatchEvent(new Event("estadisticas:actualizar"));


                } catch (err) {
                    //console.error("Error eliminando artista", err);
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
