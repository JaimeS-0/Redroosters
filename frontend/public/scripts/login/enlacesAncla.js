
// para que los enlaces de abajo del formulario pueda girar la Card

document.addEventListener("DOMContentLoaded", () => {

    const interruptor = document.getElementById("authToggle")

    // Enlaces dentro de los formularios
    const enlaceRegistro = document.getElementById("linkRegistro")
    const enlaceLogin = document.getElementById("linkLogin")

    // Cuando el usuario pulsa "Regístrate aquí"
    if (interruptor && enlaceRegistro) {
        enlaceRegistro.addEventListener("click", (e) => {

            e.preventDefault()
            interruptor.checked = true // Cambiamos al modo Registro
            window.scrollTo({ top: 0, behavior: "smooth" }) // Subimos suavemente al inicio
        })
    }

    // Cuando el usuario pulsa "Inicia sesión aquí"
    if (interruptor && enlaceLogin) {
        enlaceLogin.addEventListener("click", (e) => {
            e.preventDefault()
            interruptor.checked = false // Cambiamos al modo Iniciar sesión
            window.scrollTo({ top: 0, behavior: "smooth" })
        })
    }
})