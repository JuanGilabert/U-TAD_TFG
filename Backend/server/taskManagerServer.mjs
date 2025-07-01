// Importamos el modulo de la carpeta utils.
import { commonJSModule } from "../utils/CommonJsModuleImporter.mjs";
// Modulos de node.
import express from "express";
import { helmet } from "helmet";
// Importamos los valores de las rutas/endpoints
import {
    API_SERVER_PROCESS_NAME, ADMIN_ROUTE_PATH, USER_ROUTE_PATH, ART_ROUTE_PATH, FOOD_ROUTE_PATH,
    HEALTH_ROUTE_PATH, MEETING_ROUTE_PATH, SPORT_ROUTE_PATH, TRAVEL_ROUTE_PATH, WORK_ROUTE_PATH
} from '../config/GenericEnvConfig.mjs';
// Importamos los middlewares necesarios.
import { corsMiddleware } from "../middlewares/corsMiddleware.mjs";
import { authMiddleware } from "../middlewares/auth/authMiddleware.mjs";
import { requestMiddleware } from "../middlewares/requestMiddleware.mjs";
import { adminRequestMiddleware } from "../middlewares/admin/adminRequestMiddleware.mjs";
import { userRequestMiddleware } from "../middlewares/user/userRequestMiddleware.mjs";
import { artRequestMiddleware } from "../middlewares/art/artRequestMiddleware.mjs";
import { foodRequestMiddleware } from "../middlewares/food/foodRequestMiddleware.mjs";    
import { healthRequestMiddleware } from "../middlewares/health/healthRequestMiddleware.mjs";
import { meetingRequestMiddleware } from "../middlewares/meeting/meetingRequestMiddleware.mjs";
import { sportRequestMiddleware } from "../middlewares/sport/sportRequestMiddleware.mjs";
import { travelRequestMiddleware } from "../middlewares/travel/travelRequestMiddleware.mjs";
import { workRequestMiddleware } from "../middlewares/work/workRequestMiddleware.mjs";
// Importamos las rutas para redirigir las peticiones a los controladores del router adecuado.
import { adminRouter } from "../routes/admin/adminRouter.mjs";
import { userRouter } from "../routes/user/userRouter.mjs";
import { artRouter } from "../routes/art/artRouter.mjs";
import { foodRouter } from "../routes/food/foodRouter.mjs";
import { healthRouter } from "../routes/health/healthRouter.mjs";
import { meetingRouter } from "../routes/meeting/meetingRouter.mjs";
import { sportRouter } from "../routes/sport/sportRouter.mjs";
import { travelRouter } from "../routes/travel/travelRouter.mjs";
import { workRouter } from "../routes/work/workRouter.mjs";
// Importamos los modelos para su inyeccion.
import { AdminModel } from '../models/admin/adminModel.mjs';
import { UserModel } from '../models/user/userModel.mjs';
import { CinemaModel } from '../models/art/cinema/cinemaModel.mjs';
import { MusicModel } from '../models/art/music/musicModel.mjs';
import { PaintingModel } from '../models/art/painting/paintingModel.mjs';
import { FoodModel } from '../models/food/foodModel.mjs';
import { MedicamentModel } from '../models/health/medicament/medicamentModel.mjs';
import { MedicalAppointmentModel } from '../models/health/medicalAppointment/medicalAppointmentModel.mjs';
import { MeetingModel } from '../models/meeting/meetingModel.mjs';
import { SportModel } from '../models/sport/sportModel.mjs';
import { TravelModel } from '../models/travel/travelModel.mjs';
import { WorkModel } from '../models/work/workModel.mjs';
//// Inicializamos App Express
export const app = express();
// Middleware para procesar los cuerpos en formato JSON
app.use(
    express.json());
// Middleware para procesar los datos de formulario.
app.use(
    express.urlencoded({ extended: true }));
// Middleware para procesar las cookies.
app.use(
    commonJSModule('cookie-parser')());
// Middleware para habilitar CORS.
app.use(
    corsMiddleware());
// Establecemos como origen ...
// app.use(corsMiddleware({ acceptedOrigins: ['http://localhost:8080'] }));
// Deshabilitamos la cabecera que indica que se usa Express y su version.
app.disable('x-powered-by');
// Habilitamos el modo estricto de rutas ya que por defecto esta deshabilitado. app.enable('strict routing');
// Helmet es un middleware que agrega headers HTTP de seguridad.
app.use(helmet());
//// Administracion
// Ruta para el apartado de administracion. /api/admin
app.use(
    `${API_SERVER_PROCESS_NAME}${ADMIN_ROUTE_PATH}`,
    requestMiddleware, adminRequestMiddleware, adminRouter({ AdminModel })
);
//// Usuarios
// Ruta para el apartado del usuario. /api/user
app.use(
    `${API_SERVER_PROCESS_NAME}${USER_ROUTE_PATH}`,
    requestMiddleware, userRequestMiddleware, userRouter({ UserModel })
);
// Ruta para el apartado de arte. /api/art
app.use(
    `${API_SERVER_PROCESS_NAME}${ART_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, artRequestMiddleware,
    artRouter({ CinemaModel, MusicModel, PaintingModel })
);
// Ruta para el apartado de comida. /api/food
app.use(
    `${API_SERVER_PROCESS_NAME}${FOOD_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, foodRequestMiddleware,
    foodRouter({ FoodModel })
);
// Ruta para el apartado de salud. /api/health
app.use(
    `${API_SERVER_PROCESS_NAME}${HEALTH_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, healthRequestMiddleware,
    healthRouter({ MedicamentModel, MedicalAppointmentModel })
);
// Ruta para el apartado de citas. /api/meeting
app.use(
    `${API_SERVER_PROCESS_NAME}${MEETING_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, meetingRequestMiddleware,
    meetingRouter({ MeetingModel })
);
// Ruta para el apartado de deportes. /api/sport
app.use(
    `${API_SERVER_PROCESS_NAME}${SPORT_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, sportRequestMiddleware,
    sportRouter({ SportModel })
);
// Ruta para el apartado de viajes. /api/travel
app.use(
    `${API_SERVER_PROCESS_NAME}${TRAVEL_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, travelRequestMiddleware,
    travelRouter({ TravelModel })
);
// Ruta para el apartado de trabajo. /api/work
app.use(
    `${API_SERVER_PROCESS_NAME}${WORK_ROUTE_PATH}`,
    authMiddleware, requestMiddleware, workRequestMiddleware,
    workRouter({ WorkModel })
);
// En cualquier caso devolvemos 404 al no encontrar la ruta especificada.
app.use(
    (req, res) => {
    return res.status(404).send({ message: "Recurso no encontrado. Revise el enlace del recurso solicitado." });
});