document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll('input[type="file"]').forEach((input) => {
        const preview = input.closest("div")?.querySelector(
            "[data-preview-portada],[data-preview-portada-crear],[data-preview-portada-editar],[data-preview-audio],[data-preview-audio-crear],[data-preview-audio-editar]"
        );
        if (!preview) return;

        // ----- Pone ✔ (check) al seleccionar un archivo y su nombre debajo del input -----
        input.addEventListener("change", () => {
            if (input.files && input.files.length > 0) {
                const file = input.files[0];
                preview.textContent = "✔ Archivo seleccionado: " + file.name;
                preview.style.color = "#4ade80";
            } else {
                preview.textContent = "Ningun archivo seleccionado";
                preview.style.color = "#f97373";
            }
        });

        // ----- Limpia al enviar el formulario -----
        document.querySelectorAll("form").forEach((form) => {
            form.addEventListener("submit", () => {
                setTimeout(() => {
                    form.querySelectorAll('input[type="file"]').forEach((input) => {
                        input.value = "";

                        const preview = input.closest("div")?.querySelector(
                            "[data-preview-portada],[data-preview-portada-crear],[data-preview-portada-editar],[data-preview-audio],[data-preview-audio-crear],[data-preview-audio]"
                        );

                        if (preview) {
                            preview.textContent = "";
                        }
                    });
                }, 100);

            });
        });
    });
});

