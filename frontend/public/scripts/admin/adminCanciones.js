document.addEventListener("DOMContentLoaded", () => {
    const secciones = document.querySelectorAll(
        'section[data-uid][data-canciones]'
    );
    if (!secciones.length) return;

    secciones.forEach((root) => {
        const baseCrear = root.dataset.baseCrear;   // "/api/admin/cancion/archivo"
        const baseEditar = root.dataset.baseEditar;  // "/api/admin/cancion"
        const baseEliminar = root.dataset.baseEliminar; // "/api/admin/cancion"
        const urlCancionesPublic = root.dataset.canciones; // "/api/public/cancion"

        // Formularios
        const formCrear = root.querySelector('form[data-form="crear"]');
        const formEditar = root.querySelector('form[data-form="editar"]');
        const formEliminar = root.querySelector('form[data-form="eliminar"]');

        if (formCrear) activarLimpiezaErrores(formCrear);
        if (formEditar) activarLimpiezaErrores(formEditar);
        if (formEliminar) activarLimpiezaErrores(formEliminar);


        // Selects
        const selCancionEditar = root.querySelector("#select-cancion-editar");
        const selCancionEliminar = root.querySelector("#select-cancion-eliminar");

        // Selects de artista/album en CREAR
        const selArtistaCrear = root.querySelector("#select-artista-crear");
        const selAlbumCrear = root.querySelector("#select-album-crear");

        // Selects de artista/album en EDITAR
        const selArtistaEditar = root.querySelector("#select-artista-editar");
        const selAlbumEditar = root.querySelector("#select-album-editar");

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

            if (window.jQuery && $.fn.select2) {
                if (tab === "editar" && selArtistaEditar) {
                    $(selArtistaEditar).select2({ width: "100%" });
                }

            }
        }

        root.addEventListener("click", (e) => {
            const btn = e.target.closest("[data-tab]");
            if (!btn) return;
            activar(btn.dataset.tab);
        });

        // Inicio en "crear"
        activar("crear");

        // --------- Mensajes ---------
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
                //console.log("Body error backend:", text);
                throw new Error(`HTTP ${res.status}`);
            }
            return res;
        }

        async function recargarSelectCanciones() {
            if (!urlCancionesPublic) return;
            if (!selCancionEditar && !selCancionEliminar) return;

            try {
                const res = await fetch(urlCancionesPublic);
                if (!res.ok) {

                    return;
                }
                const data = await res.json();
                const canciones = Array.isArray(data)
                    ? data
                    : data.content ?? [];

                if (selCancionEditar) {
                    selCancionEditar.innerHTML =
                        '<option value="">Selecciona una cancion</option>';
                }
                if (selCancionEliminar) {
                    selCancionEliminar.innerHTML =
                        '<option value="">Selecciona una cancion</option>';
                }

                canciones.forEach((c) => {
                    const texto = c.titulo;

                    if (selCancionEditar) {
                        const opt = document.createElement("option");
                        opt.value = c.id;
                        opt.textContent = texto;
                        selCancionEditar.appendChild(opt);
                    }

                    if (selCancionEliminar) {
                        const opt2 = document.createElement("option");
                        opt2.value = c.id;
                        opt2.textContent = texto;
                        selCancionEliminar.appendChild(opt2);
                    }
                });

                if (window.jQuery && $.fn.select2) {
                    if (selCancionEditar) $(selCancionEditar).select2();
                    if (selCancionEliminar) $(selCancionEliminar).select2();
                    if (selArtistaCrear) $(selArtistaCrear).select2();
                    if (selAlbumCrear) $(selAlbumCrear).select2();
                    if (selArtistaEditar) $(selArtistaEditar).select2();
                    if (selAlbumEditar) $(selAlbumEditar).select2();

                }

            } catch (err) {
                //console.error("Error recargando canciones", err);
            }
        }

        // ESCUCHAR CAMBIOS DE ARTISTAS (para refrescar selects dependientes)
        document.addEventListener("artistas-actualizados", () => {
            recargarSelectCanciones();
        });

        // Cargar selects al entrar
        recargarSelectCanciones();

        // --------------Errores-------------

        // Limpia todos los errores de campos dentro de un formulario
        function clearFieldErrors(form) {
            if (!form) return;

            // Vaciar los <p>
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
            form.querySelectorAll("input[name], textarea[name]").forEach((input) => {
                input.addEventListener("input", () => {
                    const field = input.name;
                    const error = form.querySelector(`[data-error="${field}"]`);

                    input.classList.remove("input-error", "border-red-500", "ring-2", "ring-red-500/70");
                    if (error) error.textContent = "";
                });


            });

            // Para selects (artistaId y albumId)
            form.querySelectorAll("select[name]").forEach((select) => {
                select.addEventListener("change", () => {
                    const field = select.name;
                    const error = form.querySelector(`[data-error="${field}"]`);

                    select.classList.remove("input-error", "border-red-500", "ring-2", "ring-red-500/70");
                    if (error) error.textContent = "";

                });
            });
        }




        // --------- CREAR CANCION (POST api/admin/cancion/archivo) ---------
        if (formCrear) {
            formCrear.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseCrear) return;


                try {
                    const formDataOriginal = new FormData(formCrear);

                    const titulo = formDataOriginal.get("titulo");
                    const descripcion = formDataOriginal.get("descripcion") || null;
                    const artistaId = formDataOriginal.get("artistaId");
                    const albumIdRaw = formDataOriginal.get("albumId");

                    const audioFile = formDataOriginal.get("audio");
                    const portadaFile = formDataOriginal.get("portada");

                    clearFieldErrors(formCrear);
                    let hayError = false

                    if (!titulo || !titulo.trim()) {
                        setFieldError(formCrear, "titulo", "El titulo no puede estar vacio");
                        hayError = true;
                    }


                    if (!(audioFile instanceof File) || audioFile.size === 0) {
                        setFieldError(formCrear, "audio", "Debes seleccionar un archivo de audio (.mp3)");
                        hayError = true;
                    }

                    if (!artistaId || !artistaId.trim()) {
                        setFieldError(formCrear, "artistaId", "El artista no puede estar vacio");
                        hayError = true;
                    }

                    // Si hay errores, no llamamos al backend
                    if (hayError) {
                        setMsg(msgCrear, false, "Revisa los campos marcados en rojo");
                        return;
                    }

                    const dto = {
                        titulo: titulo,
                        descripcion: descripcion,
                        artistaId: Number(artistaId),
                        albumId: albumIdRaw ? Number(albumIdRaw) : null,
                    };

                    const fd = new FormData();
                    fd.append("datos", JSON.stringify(dto));
                    fd.append("audio", audioFile);

                    if (portadaFile && portadaFile.size > 0) {
                        fd.append("portada", portadaFile);
                    }


                    await fetchAdmin(baseCrear, {
                        method: "POST",
                        body: fd,
                    });

                    if (window.jQuery && $.fn.select2) {
                        if (selCancionEditar) $(selCancionEditar).select2();
                        if (selCancionEliminar) $(selCancionEliminar).select2();
                    }

                    setMsg(msgCrear, true, "Cancion creada correctamente ✅");
                    formCrear.reset();

                    // Actualizar estadísticas
                    document.dispatchEvent(new CustomEvent("artistas-actualizados"));


                    await recargarSelectCanciones();

                } catch (err) {
                    //console.error("Error creando cancion", err);
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
                            "Error al crear la cancion ❌ "
                        );
                    }
                }
            });
        }

        // ========= EDITAR CANCION (PUT /api/admin/cancion/{id}) =========
        if (formEditar && selCancionEditar) {
            formEditar.addEventListener("submit", async (e) => {
                e.preventDefault();
                if (!baseEditar) return;

                clearFieldErrors(formEditar);


                try {
                    const id = selCancionEditar.value; // <- ID de la cancion

                    const raw = new FormData(formEditar);

                    const titulo = raw.get("titulo") || null;
                    const descripcion = raw.get("descripcion") || null;
                    const artistaIdRaw = raw.get("artistaId");
                    const artistaId = artistaIdRaw ? Number(artistaIdRaw) : null;

                    let albumId = raw.get("albumId");
                    albumId = albumId ? Number(albumId) : null;


                    let hayError = false;

                    if (!id) {
                        setFieldError(formEditar, "cancionId", "Selecciona una cancion");
                        hayError = true;
                    }

                    if (!artistaId) {
                        setFieldError(formEditar, "artistaId", "Debes seleccionar un artista");
                        hayError = true;
                    }

                    if (hayError) {
                        setMsg(msgEditar, false, "Revisa los campos marcados en rojo");
                        return;
                    }

                    const dto = {
                        titulo,
                        descripcion,
                        artistaId,
                        albumId,
                    };

                    const fd = new FormData();
                    fd.append("datos", JSON.stringify(dto));

                    const audioFile = raw.get("audio");
                    if (audioFile instanceof File && audioFile.size > 0) {
                        fd.append("audio", audioFile);
                    }

                    const portadaFile = raw.get("portada");
                    if (portadaFile instanceof File && portadaFile.size > 0) {
                        fd.append("portada", portadaFile);
                    }



                    await fetchAdmin(`${baseEditar}/${id}`, {
                        method: "PUT",
                        body: fd,
                    });

                    if (window.jQuery && $.fn.select2) {
                        if (selCancionEditar) $(selCancionEditar).select2();
                        if (selCancionEliminar) $(selCancionEliminar).select2();
                    }

                    formEditar.reset();

                    setMsg(msgEditar, true, "Cancion editada correctamente ✅");
                    await recargarSelectCanciones();


                } catch (err) {
                    //console.error("Error editando cancion", err);
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
                            "Error al editar la cancion ❌"
                        );
                    }
                }
            });
        }

        // ========= ELIMINAR CANICON (DELETE /api/admin/cancion/{id}) =========
        if (formEliminar && selCancionEliminar) {
            formEliminar.addEventListener("submit", async (e) => {
                e.preventDefault();

                if (!baseEliminar) return;

                if (msgEliminar) {
                    msgEliminar.textContent = "";
                    msgEliminar.style.color = "";
                    msgEliminar.classList.remove("text-red-400");
                }


                clearFieldErrors(formEliminar);

                const id = selCancionEliminar.value;
                let hayError = false;

                if (!id) {
                    setFieldError(formEliminar, "eliminar", "Selecciona una cancion");
                    hayError = true;
                }

                try {
                    await fetchAdmin(`${baseEliminar}/${id}`, {
                        method: "DELETE",
                    });

                    setMsg(
                        msgEliminar,
                        true,
                        "Cancion eliminada correctamente ✅"
                    );

                    selCancionEliminar.value = "";
                    if (window.jQuery && $.fn.select2) {
                        $(selCancionEliminar).trigger("change");
                    }


                    if (window.jQuery && $.fn.select2) {
                        if (selCancionEditar) $(selCancionEditar).select2();
                        if (selCancionEliminar) $(selCancionEliminar).select2();
                    }

                    await recargarSelectCanciones();

                    // Actualizar estadísticas
                    window.dispatchEvent(new Event("estadisticas:actualizar"));



                } catch (err) {
                    //console.error("Error eliminando cancion", err);
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
                            "Error al eliminar la cancion ❌"
                        );
                    }
                }
            });
        }
    });
});
