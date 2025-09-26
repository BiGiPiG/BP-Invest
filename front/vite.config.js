import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    host: true,
    proxy: {
      '/bp-invest/api': {
        target: 'http://backend:8081',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/bp-invest\/api/, '/bp-invest/api'),
      },
    },
  },
})














