document.addEventListener("DOMContentLoaded", () => {

    const formLogin = document.getElementById("formLogin")
    const btnLogin = document.getElementById("btnLogin")

    if (!formLogin) return

    let enviando = false

    formLogin.addEventListener("submit", async (e) => {
        e.preventDefault()

        if (enviando) return
        enviando = true

        if (btnLogin) {
            btnLogin.disabled = true
            btnLogin.textContent = "Iniciando..."
        }

        const loginEmail = document.getElementById("login-email")
        const loginPassword = document.getElementById("login-password")

        const email = loginEmail.value.trim()
        const password = loginPassword.value.trim()

        const errorEmail = document.getElementById("login-error-email")
        const errorPassword = document.getElementById("login-error-password")

        const mensajeLogin = document.getElementById("login-mensaje")


        if (errorEmail) errorEmail.textContent = ""
        if (errorPassword) errorPassword.textContent = ""

        let valido = true

        if (!email) {
            if (errorEmail) errorEmail.textContent = "Introduce tu email."
            valido = false
        }

        if (!password) {
            if (errorPassword) errorPassword.textContent = "Introduce tu contraseña."
            valido = false
        } else if (password.length < 10) {
            if (errorPassword)
                errorPassword.textContent = "La contraseña debe tener al menos 10 caracteres."
            valido = false
        }

        if (!valido) {
            enviando = false
            if (btnLogin) {
                btnLogin.disabled = false
                btnLogin.textContent = "Iniciar sesión"
            }

            return
        }

        try {
            const res = await fetch("/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            })

            if (!res.ok) {
                let mensaje = "No se ha podido iniciar sesion."

                try {
                    const errorBody = await res.json()
                    if (errorBody.detail) mensaje = errorBody.detail
                    else if (errorBody.title) mensaje = errorBody.title
                    if (res.status === 401 || res.status === 403)
                        mensaje = "Email o contraseña incorrectos."
                } catch { }

                if (errorEmail) errorEmail.textContent = mensaje

                loginEmail.addEventListener("input", () => {
                    if (errorEmail) errorEmail.textContent = ""
                });
                loginPassword.addEventListener("input", () => {
                    if (errorPassword) errorPassword.textContent = ""
                });

                // resetear estado del envio para volver a pulsar el boton
                enviando = false
                if (btnLogin) {
                    btnLogin.disabled = false;
                    btnLogin.textContent = "Iniciar sesión"
                }
                return;
            }

            const data = await res.json()
            //console.log("✅ Login correcto:", data)

            if (mensajeLogin) {
                mensajeLogin.textContent = "Te has Logueado correctamente."
                mensajeLogin.className = "text-green-500 text-center mt-4"
            }

            document.cookie = `rr_token=${data.token}; path=/; max-age=86400`

            localStorage.setItem(
                "rr_user",
                JSON.stringify({
                    username: data.username,
                    email: data.email,
                    role: data.role,
                })
            );

            // esperar 200 ms antes de redirigir
            setTimeout(() => {
                window.location.href = "/"
            }, 200)

        } catch (err) {
            console.error(err);

            const errorEmail = document.getElementById("login-error-email");
            if (errorEmail)
                errorEmail.textContent = "No se pudo conectar con el servidor"

            enviando = false;
            if (btnLogin) {
                btnLogin.disabled = false
                btnLogin.textContent = "Iniciar sesión"
            }
        }
    });
});
