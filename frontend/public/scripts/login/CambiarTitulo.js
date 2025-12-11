document.addEventListener("DOMContentLoaded", () => {
    const titulo = document.getElementById("tituloLogin");

    if (!titulo) return;

    // Leemos el user del localStorage
    const userData = localStorage.getItem("rr_user");

    if (!userData) {
        // No hay sesion no hacemos nada
        return;
    }

    let user;
    try {
        user = JSON.parse(userData);
    } catch (e) {
        // console.error("Error rr_user:", e);
        return;
    }

    // leemos el token de la cookie
    const tokenMatch = document.cookie.match(/rr_token=([^;]+)/);
    const token = tokenMatch ? tokenMatch[1] : null;

    // console.log("-> user:", user, "token:", token)

    // Si existe usuario, cambiamos el texto
    titulo.textContent = user.username
        ? `👋🏻 ¡Bienvenido, ${user.username}! ya has iniciado sesión`
        : "👋🏻 ¡Bienvenido de nuevo!";
});