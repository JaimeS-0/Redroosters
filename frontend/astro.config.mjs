
import { defineConfig } from 'astro/config';
import tailwind from "@astrojs/tailwind";

// Proxy para desarrollo
// comentar para produccion

export default defineConfig({
  output: "static",
  integrations: [
    tailwind(),
  ],
  vite: {
    server: {
      proxy: {
        '/api/': {
          target: 'http://backend:9000',
          changeOrigin: true,
        },
      },
    },
  },
});



