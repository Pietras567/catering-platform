import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'path'
import * as fs from "node:fs";
import svgr from 'vite-plugin-svgr'

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        react(),
        tailwindcss(),
        svgr({
            include: "**/*.svg?react",
            exclude: "",
        })
    ],
    server: {
        https: (() => {
            const keyPath = path.resolve(__dirname, 'ssl/localhost-key.pem')
            const certPath = path.resolve(__dirname, 'ssl/localhost.pem')

            if (!fs.existsSync(keyPath)) {
                console.error(`Key file does not exist: ${keyPath}`)
                return false
            }

            if (!fs.existsSync(certPath)) {
                console.error(`Certificate file does not exist: ${certPath}`)
                return false
            }

            return {
                key: fs.readFileSync(keyPath),
                cert: fs.readFileSync(certPath)
            }
        })(),
        port: 5173,
        host: 'localhost'
    }
})
