import cors from 'cors'
//
const ACCEPTEDD_ORIGINS = [
    'http://localhost:8080',
    'http://localhost:2793'
]
// Exportamos el middleware
export const corsMiddleware = ({ acceptedOrigins = ACCEPTEDD_ORIGINS } = {}) => cors({
    origin: (origin, callback) => {
        //
        if (acceptedOrigins.includes(origin)) return callback(null, true)
        //
        if (!origin) return callback(null, true)
        //
        return callback(new Error("Not allowed by CORS."))
    }
});