// Carga de dotenv por seguridad.
import dotenv from 'dotenv';
dotenv.config();
// Modulos de node.
import { ReturnDocument } from 'mongodb';
//## Fichero de configuracion que almacenara en constantes exportadas todas las variables de entorno.
//// Base de datos
//  Configuraciones relacionadas con la base de datos.
const DB_USER = encodeURIComponent(process.env.DB_USER);
const DB_PWD = encodeURIComponent(process.env.DB_PWD);
const DB_HOST = process.env.DB_HOST;
export const MONGODB_URI = `mongodb+srv://${DB_USER}:${DB_PWD}@${DB_HOST}/?retryWrites=true&w=majority&appName=Cluster0`;
export const DB_NAME = process.env.DB_NAME;
export const RETURN_DOCUMENT_VALUE = ReturnDocument.DEFAULT;
export const RETURN_DOCUMENT_AFTER_VALUE = ReturnDocument.AFTER;
// Nombres de las colecciones.
export const CINEMA_COLLECTION_NAME = process.env.CINEMA_COLLECTION_NAME;
export const MUSIC_COLLECTION_NAME = process.env.MUSIC_COLLECTION_NAME;
export const PAINTING_COLLECTION_NAME = process.env.PAINTING_COLLECTION_NAME;
export const USER_COLLECTION_NAME = process.env.USER_COLLECTION_NAME;
export const FASHION_COLLECTION_NAME = process.env.FASHION_COLLECTION_NAME;
export const FOOD_COLLECTION_NAME = process.env.FOOD_COLLECTION_NAME;
export const MEDICAL_APPOINTMENT_COLLECTION_NAME = process.env.MEDICAL_APPOINTMENT_COLLECTION_NAME;
export const MEDICAMENT_COLLECTION_NAME = process.env.MEDICAMENT_COLLECTION_NAME;
export const MEETING_COLLECTION_NAME = process.env.MEETING_COLLECTION_NAME;
export const SPORT_COLLECTION_NAME = process.env.SPORT_COLLECTION_NAME;
export const TRAVEL_COLLECTION_NAME = process.env.TRAVEL_COLLECTION_NAME;
export const WORK_COLLECTION_NAME = process.env.WORK_COLLECTION_NAME;
//// Seguridad
// Puerto de escucha. Forzamos base 10 para evitar cosas raras como 0x10.
export const SERVER_BIND_PORT = parseInt(process.env.SERVER_BIND_PORT, 10) || 0;
// Admin credentials.
export const ADMIN_ROLE_PASSWORD = process.env.ADMIN_ROLE_PASSWORD;
// JWT credentials.
export const JWT_SECRET_KEY = process.env.JWT_SECRET_KEY;
// Hash Salt Rounds.
export const HASH_SALT_ROUNDS = parseInt(process.env.HASH_SALT_ROUNDS);
//// Respuestas HTTP. Configuraciones relacionadas con los mensajes de respuesta del servidor.
// Respuestas de tipo OK 2xx.
export const OKEY_200_MESSAGE = process.env.OKEY_200_MESSAGE;
export const CREATED_201_MESSAGE = process.env.CREATED_201_MESSAGE;
export const NO_CONTENT_204_MESSAGE = process.env.NO_CONTENT_204_MESSAGE;
// Respuestas de tipo Error Cliente 4xx.
export const BAD_REQUEST_400_MESSAGE = process.env.BAD_REQUEST_400_MESSAGE;
export const BAD_REQUEST_400_QUERY_MESSAGE = process.env.BAD_REQUEST_400_QUERY_MESSAGE;
export const UNAUTHORIZED_401_MESSAGE = process.env.UNAUTHORIZED_401_MESSAGE;
export const FORBIDDEN_403_MESSAGE = process.env.FORBIDDEN_403_MESSAGE;
export const NOT_FOUND_404_MESSAGE = process.env.NOT_FOUND_404_MESSAGE;
export const NOT_FOUND_404_QUERY_MESSAGE = process.env.NOT_FOUND_404_QUERY_MESSAGE;
export const NOT_ACCEPTABLE_406_MESSAGE = process.env.NOT_ACCEPTABLE_406_MESSAGE;
export const CONFLICT_409_MESSAGE = process.env.CONFLICT_409_MESSAGE;
export const UNPROCESSABLE_ENTITY_422_MESSAGE = process.env.UNPROCESSABLE_ENTITY_422_MESSAGE;
// Respuestas de tipo Error Servidor 5xx.
export const INTERNAL_SERVER_ERROR_500_MESSAGE = process.env.INTERNAL_SERVER_ERROR_500_MESSAGE;
//// Configuraciones de las Rutas.
export const API_SERVER_PROCESS_NAME = process.env.API_SERVER_PROCESS_NAME;
export const ITEM_ROUTE_PATH = process.env.ITEM_ROUTE_PATH;
export const TASK_ROUTE_PATH = process.env.TASK_ROUTE_PATH;
export const EVENT_ROUTE_PATH = process.env.EVENT_ROUTE_PATH;// /event Establecer en: fashion, 
export const APPOINTMENT_ROUTE_PATH = process.env.APPOINTMENT_ROUTE_PATH;
export const UNAVAILABLE_DATES_ROUTE_PATH = process.env.UNAVAILABLE_DATES_ROUTE_PATH;
export const IDENTIFIER_ROUTE_PATH = process.env.IDENTIFIER_ROUTE_PATH;
// Admin
export const ADMIN_ROUTE_PATH = process.env.ADMIN_ROUTE_PATH;
export const ADMIN_AUTH_ROUTE_PATH = process.env.ADMIN_AUTH_ROUTE_PATH;
export const SIGNIN_ROUTE_PATH = process.env.SIGNIN_ROUTE_PATH;
export const SIGNOUT_ROUTE_PATH = process.env.SIGNOUT_ROUTE_PATH;
// User
export const USER_ROUTE_PATH = process.env.USER_ROUTE_PATH;
// Art
export const ART_ROUTE_PATH = process.env.ART_ROUTE_PATH;
export const ART_CINEMA_ROUTE_PATH = process.env.ART_CINEMA_ROUTE_PATH;
export const CINEMA_MOVIE_DOWNLOADER_ROUTE_PATH = process.env.CINEMA_MOVIE_DOWNLOADER_ROUTE_PATH;

export const ART_MUSIC_ROUTE_PATH = process.env.ART_MUSIC_ROUTE_PATH;
export const MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH = process.env.MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH;

export const ART_PAINTING_ROUTE_PATH = process.env.ART_PAINTING_ROUTE_PATH;
export const PAINTING_EXPOSURE_ROUTE_PATH = process.env.PAINTING_EXPOSURE_ROUTE_PATH;
// Food
export const FOOD_ROUTE_PATH = process.env.FOOD_ROUTE_PATH;
export const FOOD_RESTAURANT_ROUTE_PATH = process.env.FOOD_RESTAURANT_ROUTE_PATH;
export const FOOD_SHOPPING_LIST_ROUTE_PATH = process.env.FOOD_SHOPPING_LIST_ROUTE_PATH;
// Health
export const HEALTH_ROUTE_PATH = process.env.HEALTH_ROUTE_PATH;
export const HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH = process.env.HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH;
// agregar aqui alguna variante de la ruta medical-appointment
export const HEALTH_MEDICAMENT_ROUTE_PATH = process.env.HEALTH_MEDICAMENT_ROUTE_PATH;
export const MEDICAMENT_EXPIRATION_DATE_ROUTE_PATH = process.env.MEDICAMENT_EXPIRATION_DATE_ROUTE_PATH;
// Meeting
export const MEETING_ROUTE_PATH = process.env.MEETING_ROUTE_PATH;// /meeting
// Sport
export const SPORT_ROUTE_PATH = process.env.SPORT_ROUTE_PATH;
export const SPORT_ACTIVITY_ROUTE_PATH = process.env.SPORT_ACTIVITY_ROUTE_PATH;
export const SPORT_ROUTINE_ROUTE_PATH = process.env.SPORT_ROUTINE_ROUTE_PATH;
export const ROUTINE_ESTADISTICS_ROUTE_PATH = process.env.ROUTINE_ESTADISTICS_ROUTE_PATH;
// Travel
export const TRAVEL_ROUTE_PATH = process.env.TRAVEL_ROUTE_PATH;
export const TRAVEL_TRIP_ROUTE_PATH = process.env.TRAVEL_TRIP_ROUTE_PATH;
// Work
export const WORK_ROUTE_PATH = process.env.WORK_ROUTE_PATH;