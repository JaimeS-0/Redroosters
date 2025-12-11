document.addEventListener("DOMContentLoaded", () => {

    const root = document.querySelector("[data-busqueda]");
    if (!root) {
        return;
    }

    const input = root.querySelector("[data-search-input]");
    const cajaResultado = root.querySelector("[data-search-results]");
    const ENDPOINT = "/api/public/buscar";

    // Esperar antes de buscar, cada letra dispara un evento para cragar resultados en tiempo real
    let timeoutId = null;

    // Cancela pticiones viejas para evitar mezcvlar resultados
    let controller = null;

    const limpiarResultados = () => {
        cajaResultado.innerHTML = "";
        cajaResultado.classList.add("hidden");
    };

    const pintarResultados = (resultados) => {
        cajaResultado.innerHTML = "";
        cajaResultado.classList.remove("hidden");

        if (!resultados.length) {
            const p = document.createElement("p");
            p.className = "px-4 py-3 text-sm text-gray-400";
            p.textContent = "No se han encontrado resultados";
            cajaResultado.appendChild(p);
            return;
        }

        resultados.forEach((item) => {
            let tipoTexto = "";
            let url = "#";

            switch (item.tipo) {
                case "ARTISTA":
                    tipoTexto = "Artista";
                    url = `/artista/${item.id}`;
                    break;
                case "ALBUM":
                    tipoTexto = "Album";
                    url = `/album/${item.id}`;
                    break;
                case "CANCION":
                    tipoTexto = "Cancion";
                    url = `/cancion/${item.id}`;
                    break;
            }

            const a = document.createElement("a");
            a.href = url;
            a.className =
                "flex items-center gap-4 px-5 py-4 border-b border-white/5 last:border-none hover:bg-gray-800/80 transition-colors";

            const cont = document.createElement("div");
            cont.className = "flex flex-col leading-tight";

            const titulo = document.createElement("p");
            titulo.className = "text-sm font-medium text-white";
            titulo.textContent = item.titulo;

            const subt = document.createElement("p");
            subt.className = "text-xs text-gray-400";

            const subtituloTexto = item.subtitulo
                ? `${tipoTexto} · ${item.subtitulo}`
                : tipoTexto;

            subt.textContent = subtituloTexto;

            cont.appendChild(titulo);
            cont.appendChild(subt);
            a.appendChild(cont);
            cajaResultado.appendChild(a);
        });
    };


    const buscar = async (q) => {
        if (controller) {
            controller.abort(); // cancelar peticion anterior si seguimos escribiendo
        }
        controller = new AbortController();

        try {
            cajaResultado.innerHTML = `
                <p class="px-4 py-3 text-sm text-gray-400">
                    Buscando...
                </p>
            `;
            cajaResultado.classList.remove("hidden");

            const res = await fetch(
                `${ENDPOINT}?q=${encodeURIComponent(q)}`,
                { signal: controller.signal }
            );

            if (!res.ok) {
                throw new Error("HTTP " + res.status);
            }

            const data = await res.json();
            pintarResultados(data);
        } catch (err) {
            if (err.name === "AbortError") return; // se cancelo

            cajaResultado.innerHTML = `
                <p class="px-4 py-3 text-sm text-red-400">
                    Error al buscar. Intentalo otra vez.
                </p>
            `;
            cajaResultado.classList.remove("hidden");
        }
    };

    input.addEventListener("focus", () => {
        const q = input.value.trim();

        if (q.length >= 1) {
            buscar(q);   // volvemos a cargar resultados
        }
    });

    // Evento principal: escribir en el input
    input.addEventListener("input", () => {
        const q = input.value.trim();
        //console.log("Input:", q);


        // Si esta vacio, ocultamos resultados
        if (!q) {
            limpiarResultados();
            return;
        }

        // Debounce: esperamos un poco antes de llamar al backend
        clearTimeout(timeoutId);

        timeoutId = setTimeout(() => {
            // Exigir minimo 1 letras
            if (q.length < 1) {
                limpiarResultados();
                return;
            }
            buscar(q);
        }, 300); // esperamos 300ms 
    });

    // Cerrar resultados al hacer click fuera
    document.addEventListener("click", (e) => {
        if (!root.contains(e.target)) {
            limpiarResultados();
        }
    });
});
