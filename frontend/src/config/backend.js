//export const PUBLIC_BACKEND_URL = import.meta.env.PUBLIC_BACKEND_URL;
//export const INTERNAL_BACKEND_URL = import.meta.env.BACKEND_INTERNAL_URL;

// Para peticiones desde el navegador (cliente)
export const PUBLIC_BACKEND_URL = 
  import.meta.env.PUBLIC_BACKEND_URL || 
  process.env.PUBLIC_BACKEND_URL || 
  "";

// Para peticiones desde el servidor (SSR Node en Docker)
export const INTERNAL_BACKEND_URL = 
  import.meta.env.BACKEND_INTERNAL_URL || 
  process.env.BACKEND_INTERNAL_URL || 
  process.env.INTERNAL_BACKEND_URL || 
  "http://backend:9000";