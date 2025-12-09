document.addEventListener("DOMContentLoaded", () => {

    const formRegistro = document.getElementById("formRegistro")

    if (formRegistro) {
        formRegistro.addEventListener("submit", async (e) => {
            e.preventDefault();

            const registroNombre = document.getElementById("registro-name")
            const registroEmail = document.getElementById("registro-email")
            const registroPassword = document.getElementById("registro-password")

            const nombre = registroNombre.value.trim()
            const email = registroEmail.value.trim()
            const password = registroPassword.value.trim()

            const errorName = document.getElementById("error-name")
            const errorEmail = document.getElementById("error-email")
            const errorPassword = document.getElementById("error-password")

            const mensajeRegistro = document.getElementById("registro-mensaje")

            errorName.textContent = ""
            errorEmail.textContent = ""
            errorPassword.textContent = ""

            let valido = true

            // Validación nombre, email y password
            const usernameRegex = /^[A-Za-z][A-Za-z0-9_-]{2,14}$/ // 3-15 chars, empieza por letra

            if (!nombre) {
                errorName.textContent = "Introduce tu nombre de usuario."
                valido = false
            } else if (nombre.length < 3 || nombre.length > 15) {
                errorName.textContent = "Debe tener entre 3 y 15 caracteres."
                valido = false
            } else if (!usernameRegex.test(nombre)) {
                errorName.textContent =
                    "Debe empezar por una letra y solo usar letras, números y guiones.";
                valido = false
            }


            if (!email) {
                errorEmail.textContent = "Introduce tu email."
                valido = false
            }


            if (!password) {
                errorPassword.textContent = "Introduce tu contraseña."
                valido = false
            } else if (password.length < 10) {
                errorPassword.textContent =
                    "La contraseña debe tener al menos 10 caracteres.";
                valido = false
            }

            if (!valido) return

            // Aqui ya se registra el usuario
            try {
                const res = await fetch("/api/auth/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        name: nombre,
                        email: email,
                        password: password,
                    }),
                })

                if (!res.ok) {
                    let mensaje = "Ha habido un error al registrarte."

                    try {
                        const errorBody = await res.json()
                        if (errorBody.detail) mensaje = errorBody.detail
                        else if (errorBody.title) mensaje = errorBody.title
                        if (res.status === 409)
                            mensaje = "Ese email ya está en uso. Prueba con otro.";
                    } catch { }

                    if (errorEmail) errorEmail.textContent = mensaje

                    const emailInput = document.getElementById("registro-email")
                    if (emailInput) {
                        emailInput.addEventListener("input", () => {
                            if (errorEmail) errorEmail.textContent = ""
                        })
                    }

                    return
                }

                // Si todo va bien 

                const data = await res.json()
                // console.log(" Registrado:", data)
                if (mensajeRegistro) {
                    mensajeRegistro.textContent = "Te has registrado correctamente. Ahora puedes iniciar sesión."
                    mensajeRegistro.className = "text-green-500 text-center mt-4"
                }


                const toggle = document.getElementById("authToggle")
                if (toggle) toggle.checked = false
                window.scrollTo({ top: 0, behavior: "smooth" })

            } catch (err) {
                // console.error(err)
                if (mensajeRegistro) {
                    mensajeRegistro.textContent = "Error de conexión con el servidor. Inténtalo más tarde."
                    mensajeRegistro.className = "text-red-500 text-center mt-4"
                }
            }
        })
    }
})