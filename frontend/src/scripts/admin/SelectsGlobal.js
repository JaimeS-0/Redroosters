document.addEventListener("DOMContentLoaded", () => {
    const secciones = document.querySelectorAll("section[data-uid]");
    if (!secciones.length) return;

    // Helper generico para pedir listas publicas (Page<> o array)
    async function cargarLista(url) {
        const res = await fetch(url);
        if (!res.ok) throw new Error("HTTP " + res.status);
        const data = await res.json();
        return Array.isArray(data) ? data : data.content ?? [];
    }

    secciones.forEach((root) => {
        // Para ARTISTAS:
        // - En CancionesPanel data-artistas="/api/public/artistas"
        // - En ArtistasPanel NO hay data-artistas, pero SI base-public="/api/public/artistas"
        const urlArtistas =
            root.dataset.artistas || root.dataset.basePublic || null;

        // Para albums y canciones va directo
        const urlAlbums = root.dataset.albums || null;
        const urlCanciones = root.dataset.canciones || null;

        // ---- SELECTS DE ARTISTAS (diferenciamos crear / editar / eliminar) ----
        const selArtistaCrear = root.querySelector('[data-select-artista="crear"]');
        const selArtistaEditar = root.querySelector('[data-select-artista="editar"]');
        const selArtistaEliminar = root.querySelector('[data-select-artista="eliminar"]');

        // ---- SELECTS DE ALBUMS ----
        const selAlbumCrear = root.querySelector('[data-select-album="crear"]');
        const selAlbumEditar = root.querySelector('[data-select-album="editar"]');
        const selAlbumEliminar = root.querySelector('[data-select-album="eliminar"]');

        // ---- SELECTS DE CANCIONES ----

        const selCancionEditar = root.querySelector('[data-select-cancion="editar"]');
        const selCancionEliminar = root.querySelector('[data-select-cancion="eliminar"]');

        // ARTISTAS 
        async function recargarArtistas() {
            if (!urlArtistas) return;

            let artistas;
            try {
                artistas = await cargarLista(urlArtistas);
            } catch (err) {
                //console.error("Error recargando artistas", err);
                return;
            }

            if (selArtistaCrear) {
                selArtistaCrear.innerHTML =
                    '<option value="">Selecciona un artista</option>';
            }
            if (selArtistaEditar) {
                selArtistaEditar.innerHTML =
                    '<option value="">Selecciona un artista</option>';
            }
            if (selArtistaEliminar) {
                selArtistaEliminar.innerHTML =
                    '<option value="">Selecciona un artista</option>';
            }

            artistas.forEach((a) => {
                const opt = document.createElement("option");
                opt.value = a.id;
                opt.textContent = a.nombre ?? `Artista ${a.id}`;

                if (selArtistaCrear) {
                    selArtistaCrear.appendChild(opt.cloneNode(true));
                }
                if (selArtistaEditar) {
                    selArtistaEditar.appendChild(opt.cloneNode(true));
                }
                if (selArtistaEliminar) {
                    selArtistaEliminar.appendChild(opt.cloneNode(true));
                }
            });

            if (window.jQuery && $.fn.select2) {
                if (selArtistaCrear) $(selArtistaCrear).select2();
                if (selArtistaEditar) $(selArtistaEditar).select2();
                if (selArtistaEliminar) $(selArtistaEliminar).select2();
            }
        }

        //  ALBUMS
        async function recargarAlbums() {
            if (!urlAlbums) return;

            let albums;
            try {
                albums = await cargarLista(urlAlbums);
            } catch (err) {
                //console.error("Error recargando albums", err);
                return;
            }

            if (selAlbumCrear) {
                selAlbumCrear.innerHTML =
                    '<option value="">Sin album</option>';
            }
            if (selAlbumEditar) {
                selAlbumEditar.innerHTML =
                    '<option value="">Mantener album / sin album</option>';
            }
            if (selAlbumEliminar) {
                selAlbumEliminar.innerHTML =
                    '<option value="">Selecciona un album</option>';
            }


            albums.forEach((al) => {
                const opt = document.createElement("option");
                opt.value = al.id;
                opt.textContent = al.titulo;

                if (selAlbumCrear) {
                    selAlbumCrear.appendChild(opt.cloneNode(true));
                }
                if (selAlbumEditar) {
                    selAlbumEditar.appendChild(opt.cloneNode(true));
                }
                if (selAlbumEliminar) {
                    selAlbumEliminar.appendChild(opt.cloneNode(true));
                }

            });

            if (window.jQuery && $.fn.select2) {
                if (selAlbumCrear) $(selAlbumCrear).select2();
                if (selAlbumEditar) $(selAlbumEditar).select2();
                if (selAlbumEliminar) $(selAlbumEliminar).select2();
            }
        }

        //  CANCIONES
        async function recargarCanciones() {
            if (!urlCanciones) return;
            if (!selCancionEditar && !selCancionEliminar) return;

            let canciones;
            try {
                canciones = await cargarLista(urlCanciones);
            } catch (err) {
                //console.error("Error recargando canciones", err);
                return;
            }

            if (selCancionEditar) {
                selCancionEditar.innerHTML =
                    '<option value="">Selecciona una cancion</option>';
            }
            if (selCancionEliminar) {
                selCancionEliminar.innerHTML =
                    '<option value="">Selecciona una cancion</option>';
            }

            canciones.forEach((c) => {
                const opt = document.createElement("option");
                opt.value = c.id;
                opt.textContent = c.titulo;

                if (selCancionEditar) {
                    selCancionEditar.appendChild(opt.cloneNode(true));
                }
                if (selCancionEliminar) {
                    selCancionEliminar.appendChild(opt.cloneNode(true));
                }
            });

            if (window.jQuery && $.fn.select2) {
                if (selCancionEditar) $(selCancionEditar).select2();
                if (selCancionEliminar) $(selCancionEliminar).select2();
            }
        }

        // ========= CARGA INICIAL =========
        recargarArtistas();
        recargarAlbums();
        recargarCanciones();

        // ========= EVENTOS GLOBALES =========
        document.addEventListener("artistas-actualizados", recargarArtistas);
        document.addEventListener("albums-actualizados", recargarAlbums);
        document.addEventListener("canciones-actualizadas", recargarCanciones);
    });
});
