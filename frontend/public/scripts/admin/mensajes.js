document.addEventListener("DOMContentLoaded", () => {
    const root = document.querySelector("[data-admin-mensajes]");
    if (!root) return;

    const endpoint = root.dataset.endpoint;
    const estado = document.getElementById("mensajes-estado");
    const tbody = document.getElementById("mensajes-tbody");

    // Comprobar token
    const token = localStorage.getItem("token");
    if (!token) {
        if (estado) {
            estado.textContent = "Necesitas iniciar sesión como administrador.";
            estado.className = "text-sm text-red-400 mb-4";
        }
        return;
    }

    // Helper para crear celdas
    function crearCelda(tr, contenido, extraClasses = "") {
        const td = document.createElement("td");
        td.className = "px-6 py-4 align-top " + extraClasses;

        if (contenido instanceof Node) {
            td.appendChild(contenido);
        } else {
            td.textContent = contenido ?? "";
        }

        tr.appendChild(td);
    }

    // Helper específico para la celda de email con mailto
    function crearCeldaEmail(tr, email) {
        const wrapper = document.createElement("div");
        wrapper.className = "flex items-center gap-1";

        const icon = document.createElement("span");
        icon.textContent = "📩";
        icon.className = "text-sm";
        wrapper.appendChild(icon);

        const link = document.createElement("a");
        link.href = `mailto:${email}`;
        link.textContent = email;
        link.className = "text-blue-400 underline hover:text-blue-300";
        wrapper.appendChild(link);

        crearCelda(tr, wrapper);
    }

    // Carga de mensajes
    async function cargarMensajes() {
        try {
            if (estado) {
                estado.textContent = "Cargando mensajes...";
                estado.className = "text-sm text-gray-400 mb-4";
            }

            const res = await fetch(endpoint, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (res.status === 401 || res.status === 403) {
                if (estado) {
                    estado.textContent =
                        "No tienes permisos para ver estos mensajes (requiere rol ADMIN).";
                    estado.className = "text-sm text-red-400 mb-4";
                }
                return;
            }

            if (!res.ok) {
                throw new Error("HTTP " + res.status);
            }

            const mensajes = await res.json();
            console.log("MENSAJES:", mensajes);

            tbody.innerHTML = "";

            // Sin mensajes
            if (!mensajes || !mensajes.length) {
                const tr = document.createElement("tr");
                const td = document.createElement("td");
                td.colSpan = 4; // 4 columnas: Fecha, Nombre, Email, Mensaje
                td.className = "px-6 py-6 text-center text-gray-400";
                td.textContent = "No hay mensajes todavía.";
                tr.appendChild(td);
                tbody.appendChild(tr);

                if (estado) {
                    estado.textContent = "No hay mensajes de contacto.";
                    estado.className = "text-sm text-gray-400 mb-4";
                }
                return;
            }

            // Con mensajes
            mensajes.forEach((m) => {
                const tr = document.createElement("tr");
                tr.className =
                    "border-t border-white/10 hover:bg-white/5 transition-colors";

                let fechaTexto = "";
                const fechaRaw = m.createdAt

                if (fechaRaw) {
                    const d = new Date(fechaRaw);
                    if (!isNaN(d.getTime())) {
                        fechaTexto = d.toLocaleString("es-ES", {
                            day: "2-digit",
                            month: "2-digit",
                            year: "numeric",
                            hour: "2-digit",
                            minute: "2-digit",
                        });
                    }
                }

                crearCelda(tr, fechaTexto || "-");
                crearCelda(tr, m.nombre || "-");

                // Email clicable
                crearCeldaEmail(tr, m.email || "-");

                // Mensaje con ancho máximo y salto de línea
                crearCelda(
                    tr,
                    m.mensaje || "",
                    "max-w-xs whitespace-pre-wrap break-words"
                );

                tbody.appendChild(tr);
            });

            if (estado) {
                estado.textContent = "Mensajes cargados correctamente.";
                estado.className = "text-sm text-gray-400 mb-4";
            }
        } catch (err) {
            console.error("Error cargando mensajes", err);
            if (estado) {
                estado.textContent =
                    "Error al cargar los mensajes. Inténtalo de nuevo más tarde.";
                estado.className = "text-sm text-red-400 mb-4";
            }
        }
    }

    // 4) Llamada inicial
    cargarMensajes();
});
