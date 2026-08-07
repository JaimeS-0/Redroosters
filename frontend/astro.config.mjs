import { defineConfig } from 'astro/config';
import tailwind from "@astrojs/tailwind";
import node from "@astrojs/node";

export default defineConfig({
  output: "hybrid",
  adapter: node({
    mode: "standalone",
  }),
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



