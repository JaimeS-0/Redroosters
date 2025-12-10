document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formContacto");

    const inputNombre = document.getElementById("contacto-nombre");
    const inputEmail = document.getElementById("contacto-email");
    const inputMensaje = document.getElementById("contacto-mensaje");

    const errorNombre = document.getElementById("contacto-error-nombre");
    const errorEmail = document.getElementById("contacto-error-email");
    const errorMensaje = document.getElementById("contacto-error-mensaje");

    const mensajeEstado = document.getElementById("contacto-mensaje-estado");

    const baseUrl = form.dataset.backendUrl;
    const BACKEND_URL = `${baseUrl}/api/public/contacto`;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Limpiar errores
        errorNombre.textContent = "";
        errorEmail.textContent = "";
        errorMensaje.textContent = "";
        mensajeEstado.textContent = "";
        mensajeEstado.classList.remove("text-red-500", "text-green-500");

        // Leer valores
        const nombre = inputNombre.value.trim();
        const email = inputEmail.value.trim();
        const mensaje = inputMensaje.value.trim();

        let valid = true;

        // Valiaciones
        if (nombre.length < 2) {
            errorNombre.textContent =
                "El nombre debe tener al menos 2 caracteres";
            valid = false;
        }
        if (!email.includes("@") || email.length < 5) {
            errorEmail.textContent = "Email no valido";
            valid = false;
        }
        if (mensaje.length < 5) {
            errorMensaje.textContent = "El mensaje es demasiado corto";
            valid = false;
        }

        if (!valid) return;

        try {
            const res = await fetch(BACKEND_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ nombre, email, mensaje }),
            });

            if (res.ok) {
                mensajeEstado.textContent = "Mensaje enviado correctamente";
                mensajeEstado.classList.add("text-green-500");

                // Limpiar campos
                inputNombre.value = "";
                inputEmail.value = "";
                inputMensaje.value = "";
            } else {
                let data = null;
                try {
                    data = await res.json();
                } catch (_) { }

                if (data && data.errors) {
                    data.errors.forEach((err) => {
                        if (err.field === "nombre")
                            errorNombre.textContent = err.message;
                        if (err.field === "email")
                            errorEmail.textContent = err.message;
                        if (err.field === "mensaje")
                            errorMensaje.textContent = err.message;
                    });
                } else {
                    mensajeEstado.textContent =
                        "Ha ocurrido un error inesperado";
                    mensajeEstado.classList.add("text-red-500");
                }
            }
        } catch (error) {
            //console.error(error);
            mensajeEstado.textContent =
                "No se pudo conectar con el servidor";
            mensajeEstado.classList.add("text-red-500");
        }
    });
});
