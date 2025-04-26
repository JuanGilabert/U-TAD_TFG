// Carga de dotenv por seguridad.
import dotenv from 'dotenv';
dotenv.config();
// Modulos de node.
import { ReturnDocument } from 'mongodb';
//// Fichero de configuracion que almacenara en constantes exportadas todas las variables de entorno.
// Puerto de escucha. Forzamos base 10 para evitar cosas raras como 0x10.
export const SERVER_BIND_PORT = parseInt(process.env.SERVER_BIND_PORT, 10) || 0;
//// Configuraciones relacionadas con la base de datos.
const DB_USER = encodeURIComponent(process.env.DB_USER);
const DB_PWD = encodeURIComponent(process.env.DB_PWD);
const DB_HOST = process.env.DB_HOST;
export const MONGODB_URI = `mongodb+srv://${DB_USER}:${DB_PWD}@${DB_HOST}/?retryWrites=true&w=majority&appName=Cluster0`;
export const DB_NAME = process.env.DB_NAME;
export const RETURN_DOCUMENT_VALUE = ReturnDocument.AFTER;
// Nombres de las colecciones.
export const CINEMA_COLLECTION_NAME = process.env.CINEMA_COLLECTION_NAME;
export const MUSIC_COLLECTION_NAME = process.env.MUSIC_COLLECTION_NAME;
export const PAINTING_COLLECTION_NAME = process.env.PAINTING_COLLECTION_NAME;
export const AUTH_COLLECTION_NAME = process.env.AUTH_COLLECTION_NAME;
export const FASHION_COLLECTION_NAME = process.env.FASHION_COLLECTION_NAME;
export const FOOD_COLLECTION_NAME = process.env.FOOD_COLLECTION_NAME;
export const MEDICAL_APPOINTMENT_COLLECTION_NAME = process.env.MEDICAL_APPOINTMENT_COLLECTION_NAME;
export const MEDICAMENT_COLLECTION_NAME = process.env.MEDICAMENT_COLLECTION_NAME;
export const MEETING_COLLECTION_NAME = process.env.MEETING_COLLECTION_NAME;
export const SPORT_COLLECTION_NAME = process.env.SPORT_COLLECTION_NAME;
export const TRAVEL_COLLECTION_NAME = process.env.TRAVEL_COLLECTION_NAME;
export const WORK_COLLECTION_NAME = process.env.WORK_COLLECTION_NAME;
//// Seguridad
// JWT credentials.
export const JWT_SECRET_KEY = process.env.JWT_SECRET_KEY;
// Hash Salt Rounds.
export const HASH_SALT_ROUNDS = parseInt(process.env.HASH_SALT_ROUNDS);
//// Respuestas HTTP
// Configuraciones relacionadas con los mensajes de respuesta del servidor.
export const OKEY_200_MESSAGE = process.env.OKEY_200_MESSAGE;
export const CREATED_201_MESSAGE = process.env.CREATED_201_MESSAGE;
export const NO_CONTENT_204_MESSAGE = process.env.NO_CONTENT_204_MESSAGE;
export const BAD_REQUEST_400_MESSAGE = process.env.BAD_REQUEST_400_MESSAGE;
export const BAD_REQUEST_400_QUERY_MESSAGE = "Esta ruta no acepta query parameters";
export const UNAUTHORIZED_401_MESSAGE = process.env.UNAUTHORIZED_401_MESSAGE;
export const FORBIDDEN_403_MESSAGE = process.env.FORBIDDEN_403_MESSAGE;
export const NOT_FOUND_404_MESSAGE = process.env.NOT_FOUND_404_MESSAGE;
export const NOT_ACCEPTABLE_406_MESSAGE = process.env.NOT_ACCEPTABLE_406_MESSAGE;
export const CONFLICT_409_MESSAGE = process.env.CONFLICT_409_MESSAGE;
export const INTERNAL_SERVER_ERROR_500_MESSAGE = process.env.INTERNAL_SERVER_ERROR_500_MESSAGE;