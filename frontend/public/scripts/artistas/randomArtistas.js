document.addEventListener("DOMContentLoaded", () => {

    // Buscar la sección de artistas aleatorios
    const seccion = document.querySelector("[data-random-artistas]");
    if (!seccion) return;

    const grid = seccion.querySelector("[data-grid]");
    if (!grid) return;

    // Endpoint configurado en el atributo data-endpoint del HTML
    const endpoint = seccion.dataset.endpoint || "/api/public/artistas/random";

    // Funcion que crea el HTML de una card de artista
    const crearCard = (artista) => {
        const id = artista.id;
        const nombre = artista.nombre || "Artista";
        const portada = artista.portada || artista.portadaUrl || "";

        return `
        <a href="/artista/${id}" class="block group">
            <div class="flex flex-col items-center">
                <!-- FOTO CUADRADA -->
                <div
                    class="
                        w-48 h-48
                        sm:w-56 sm:h-56
                        md:w-64 md:h-64
                        rounded-3xl overflow-hidden
                        border border-white/10
                        bg-gray-900/80
                        shadow-[0_0_25px_rgba(0,0,0,0.6)]
                        transition-all duration-300 ease-out
                        group-hover:scale-110 group-hover:-translate-y-2
                        group-hover:shadow-[0_0_50px_rgba(255,0,0,0.45)]
                    "
                >
                    <img
                        src="${portada}"
                        alt="${nombre}"
                        class="
                            w-full h-full
                            object-cover object-center
                            transition-all duration-300 ease-out
                            group-hover:scale-110
                        "
                        loading="lazy"
                    />
                </div>

                <!-- NOMBRE -->
                <p
                    class="
                        mt-4 text-white text-sm sm:text-base font-medium
                        text-center max-w-[10rem] truncate
                        transition-all duration-300
                        group-hover:text-red-400
                    "
                >
                    ${nombre}
                </p>
            </div>
        </a>
        `;
    };

    // Pinta la lista de artistas en el grid
    const pintarArtistas = (lista) => {
        grid.innerHTML = lista
            .slice(0, 6)
            .map((artista) => crearCard(artista))
            .join("");
    };

    // Llama al backend y actualiza el grid
    const cargarArtistas = async () => {
        try {
            const url = `${endpoint}?size=6`;

            const res = await fetch(url);
            if (!res.ok) throw new Error("HTTP " + res.status);

            const data = await res.json();

            const lista = Array.isArray(data) ? data : data.content ?? [];
            pintarArtistas(lista);
        } catch (err) {
            //console.error("❌ Error cargando artistas:", err);

            grid.innerHTML = `
                <p class="col-span-3 text-center text-sm text-gray-300">
                    No se han podido cargar los artistas recomendados.
                </p>
            `;
        }
    };

    // Carga inicial al entrar en la pagina
    cargarArtistas();

    // Boton "Actualizar artistas"
    const btn = document.getElementById("btn-actualizar-artistas");

    if (btn) {
        btn.addEventListener("click", () => {
            cargarArtistas();
        });
    }
});
