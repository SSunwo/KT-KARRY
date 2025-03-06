import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  server: {
    cors: true,
    proxy: {
      '/emp': {
        target: 'http://localhost:8080',
        secure: false,
        changeOrigin: true,
        // rewrite: (path) => {
        //   return path.replace(/^\/api/, "");
        // },
      },
    },
  },
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
