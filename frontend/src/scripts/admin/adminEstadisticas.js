
// Funcion reutilizable para cargar las estadisticas en el panel
async function cargarEstadisticas() {
    const root = document.getElementById("estadisticas-panel");
    if (!root) return;

    const token = localStorage.getItem("token");
    if (!token) {
        return;
    }

    try {
        const res = await fetch("/api/admin/estadisticas", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (!res.ok) {
            throw new Error("HTTP " + res.status);
        }

        const stats = await res.json();

        const setText = (key, value) => {
            const el = root.querySelector(`[data-stat="${key}"]`);
            if (el) el.textContent = value;
        };

        setText("totalArtistas", stats.totalArtistas ?? 0);
        setText("totalCanciones", stats.totalCanciones ?? 0);
        setText("totalAlbumes", stats.totalAlbumes ?? 0);
        setText(
            "totalEscuchasGlobales",
            stats.totalEscuchasGlobales ?? 0
        );

        if (stats.cancionMasEscuchada) {
            setText(
                "cancionMasEscuchada",
                `${stats.cancionMasEscuchada.titulo} (${stats.cancionMasEscuchada.escuchas} escuchas)`
            );
        }

        if (stats.cancionConMasLikes) {
            setText(
                "cancionConMasLikes",
                `${stats.cancionConMasLikes.titulo} (${stats.cancionConMasLikes.likes} likes)`
            );
        }
    } catch (err) {
        console.error("[Estadisticas] Error cargando estadisticas", err);
    }
}

// Al cargar la pagina, y cuando haya cambios globales, recargamos las estadisticas
document.addEventListener("DOMContentLoaded", () => {
    // Carga inicial al entrar al panel
    cargarEstadisticas();

    // Escuchar un evento global para refrescar cuando se actualicen datos
    window.addEventListener("estadisticas:actualizar", () => {
        cargarEstadisticas();
    });
});
