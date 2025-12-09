document.addEventListener("DOMContentLoaded", () => {

    function initLikesPage() {
        const section = document.querySelector("[data-backend-url]");
        const BACKEND_URL = section?.dataset.backendUrl ?? "";
        const grid = document.getElementById("likes-grid");
        const message = document.getElementById("likes-message");
        const loadMoreBtn = document.getElementById("load-more-btn");

        if (!section || !grid || !message || !loadMoreBtn || !BACKEND_URL) {
            console.warn("Likes: faltan elementos o BACKEND_URL");
            return;
        }

        const token = localStorage.getItem("token");

        if (!token) {
            message.textContent =
                "Necesitas iniciar sesion para ver tus canciones guardadas.";
            loadMoreBtn.classList.add("hidden");
            setTimeout(() => {
                window.location.href = "/login";
            }, 1500);
            return;
        }

        let currentPage = 0;
        const PAGE_SIZE = 10;
        let lastPage = false;
        let isLoading = false;

        async function cargarPagina(page) {
            if (isLoading || lastPage) return;
            isLoading = true;
            loadMoreBtn.disabled = true;
            loadMoreBtn.textContent = "Cargando...";

            try {
                const res = await fetch(
                    `${BACKEND_URL}/api/user/likes?page=${page}&size=${PAGE_SIZE}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    },
                );

                if (res.status === 401 || res.status === 403) {
                    message.textContent =
                        "Tu sesion ha expirado. Redirigiendo a login...";
                    loadMoreBtn.classList.add("hidden");
                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 1500);
                    return;
                }

                if (!res.ok) {
                    console.error("Error HTTP:", res.status);
                    if (grid.children.length === 0) {
                        message.textContent = "Error cargando tus likes.";
                    }
                    loadMoreBtn.classList.add("hidden");
                    return;
                }

                const data = await res.json();

                const canciones = Array.isArray(data.content)
                    ? data.content
                    : [];

                if (canciones.length === 0 && grid.children.length === 0) {
                    message.textContent =
                        "Todavia no tienes canciones guardadas.";
                    loadMoreBtn.classList.add("hidden");
                    return;
                }

                // Primera carga: quitamos mensaje
                if (grid.children.length === 0) {
                    message.textContent = "";
                }

                // Pintar cards
                canciones.forEach((cancion) => {
                    // Crear el contenedor de la Card + Título
                    const wrapper = document.createElement("div");
                    wrapper.className =
                        "w-full mx-auto max-w-[180px] text-center";

                    // Crear el enlace de la tarjeta
                    const card = document.createElement("a");
                    card.href = `/cancion/${cancion.id}`;
                    card.className =
                        "group relative rounded-2xl overflow-hidden bg-slate-900 cursor-pointer " +
                        "shadow-lg hover:shadow-xl hover:shadow-black/50 hover:scale-[1.02] " +
                        "transition-transform duration-200 w-full block mb-2";
                    card.style.aspectRatio = "1 / 1";

                    const img = document.createElement("img");
                    const portada =
                        cancion.portada ||
                        cancion.portadaUrl ||
                        cancion.urlPortada ||
                        "";
                    img.src = portada || "";
                    img.alt = cancion.titulo ?? "Cancion";
                    img.className =
                        "w-full h-full object-cover transform scale-105 group-hover:scale-110 transition duration-300";
                    card.appendChild(img);

                    const overlay = document.createElement("div");
                    overlay.className =
                        "absolute inset-0 bg-black/10 group-hover:bg-black/30 transition-colors duration-300";
                    card.appendChild(overlay);

                    // Crear el Título de la Canción
                    const titulo = (cancion.titulo || "").toString();

                    const titleText = document.createElement("p");
                    titleText.className =
                        "mt-1 text-sm font-semibold text-gray-100 tracking-wide truncate " +
                        "drop-shadow-[0_0_4px_rgba(0,0,0,0.8)]";
                    titleText.textContent = titulo || "Cancion sin titulo";

                    wrapper.appendChild(card);
                    wrapper.appendChild(titleText);
                    grid.appendChild(wrapper);
                });

                // Gestion de paginacion
                lastPage = Boolean(data.last);
                if (lastPage) {
                    loadMoreBtn.disabled = true;
                    loadMoreBtn.textContent = "No hay mas canciones";
                    loadMoreBtn.classList.add("opacity-60");
                } else {
                    currentPage =
                        (typeof data.number === "number"
                            ? data.number
                            : page) + 1;
                    loadMoreBtn.disabled = false;
                    loadMoreBtn.textContent = "Cargar mas";
                }
            } catch (err) {
                console.error("Error cargando likes:", err);
                if (grid.children.length === 0) {
                    message.textContent =
                        "Error de red al cargar tus likes.";
                }
                loadMoreBtn.classList.add("hidden");
            } finally {
                isLoading = false;
            }
        }

        // Primera carga
        cargarPagina(currentPage);

        // Click en "Cargar mas"
        loadMoreBtn.addEventListener("click", () => {
            if (!lastPage && !isLoading) {
                cargarPagina(currentPage);
            }
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initLikesPage);
    } else {
        initLikesPage();
    }

})
