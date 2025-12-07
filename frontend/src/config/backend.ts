
// URL que VE EL NAVEGADOR (para audio, fetch desde el cliente, etc.)
export const PUBLIC_BACKEND_URL =
    import.meta.env.PUBLIC_BACKEND_URL ?? "http://localhost:9000";

// URL INTERNA del backend (solo para código que corre en el servidor: SSR / build)
export const INTERNAL_BACKEND_URL =
    import.meta.env.BACKEND_INTERNAL_URL ?? "http://backend:9000";
