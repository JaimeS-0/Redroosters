import { defineConfig } from 'astro/config';

import tailwindcss from '@tailwindcss/vite';

// Proxy para desarrollo
export default defineConfig({
  vite: {
    plugins: [tailwindcss()],
    server: {
      proxy: {
        '/api': {
          target: 'http://backend:9000',
          changeOrigin: true,
        },
      },
    },
  },
  
});



