document.addEventListener("DOMContentLoaded", () => {
    const campos = [
        { inputId: "login-password", botonId: "verContrasena" },
        { inputId: "registro-password", botonId: "verContrasenaRegistro" },
    ];

    campos.forEach(({ inputId, botonId }) => {
        const inputContrasena = document.getElementById(inputId);
        const botonVerContrasena = document.getElementById(botonId);

        if (!inputContrasena || !botonVerContrasena) return;

        botonVerContrasena.addEventListener("click", () => {
            const esOculta = inputContrasena.type === "password";

            // Cambiar tipo
            inputContrasena.type = esOculta ? "text" : "password";

            // Cambiar icono
            botonVerContrasena.textContent = esOculta
                ? `
          <!-- ojo normal -->
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
               stroke-width="1.8" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M2.25 12s3.75-6.75 9.75-6.75S21.75 12 21.75 12s-3.75 6.75-9.75 6.75S2.25 12 2.25 12z" />
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
        `
                : `
          <!-- ojo tachado -->
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
               stroke-width="1.8" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M3 3l18 18M2.25 12s3.75-6.75 9.75-6.75c2.01 0 3.78.53 5.32 1.3M19.5 8.25c1.73 1.28 2.25 2.07 2.25 2.07s-3.75 6.75-9.75 6.75c-2.01 0-3.78-.53-5.32-1.3" />
          </svg>
        `;

            // Actualizar título dejando el raton por encima
            botonVerContrasena.title = esOculta
                ? "Ocultar contraseña"
                : "Ver contraseña";
            botonVerContrasena.setAttribute(
                "aria-label",
                esOculta ? "Ocultar contraseña" : "Ver contraseña"
            );
        });
    });
});