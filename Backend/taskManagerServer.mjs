import express from "express";
import { commonJSModule } from "./utils/CommonJsModuleImporter.mjs";
// Importamos los middlewares necesarios.
import { corsMiddleware } from "./middlewares/corsMiddleware.mjs";
// Importamos las rutas para usarlas y redirigir las peticiones.
import { artRouter } from "./routes/art/artRoute.mjs"
import { authRouter } from "./routes/auth/authRoutes.mjs"
import { foodRouter } from "./routes/food/foodRoute.mjs"
import { healthRouter } from "./routes/health/healthRouter.mjs"
import { meetingRouter } from "./routes/meeting/meetingRoute.mjs"
import { sportRouter } from "./routes/sport/sportRoute.mjs"
import { travelRouter } from "./routes/travel/travelRoute.mjs"
import { workRouter } from "./routes/work/workRoute.mjs"
// Importamos los modelos para inyectarlos.
import { CinemaModel } from './models/artModels/cinemaModel/cinemaModel.mjs';
import { MusicModel } from './models/artModels/musicModel/musicModel.mjs';
import { PaintingModel } from './models/artModels/paintingModel/paintingModel.mjs';
import { AuthModel } from './models/authModels/authModel.mjs';
import { FoodModel } from './models/foodModels/foodModel.mjs';
import { MedicamentModel } from './models/healthModels/medicamentModel/medicamentModel.mjs';
import { MedicalAppointmentModel } from './models/healthModels/medicalAppointmentModel/medicalAppointmentModel.mjs';
import { MeetingModel } from './models/meetingModels/meetingModel.mjs';
import { SportModel } from './models/sportModels/sportModel.mjs';
import { TravelModel } from './models/travelModels/travelModel.mjs';
import { WorkModel } from './models/workModels/workModel.mjs';
//// Inicializamos App Express
export const app = express();
// Middleware para procesar los cuerpos en formato JSON
app.use(express.json());
// Middleware para procesar los datos de formulario.
app.use(express.urlencoded());
// Middleware para procesar las cookies.
app.use(commonJSModule('cookie-parser')());
// Middleware para habilitar CORS.
app.use(corsMiddleware());
// Establecemos como origen ...
// app.use(corsMiddleware({ acceptedOrigins: ['http://localhost:8080'] }));
// Deshabilitamos la cabecera que indica que se usa Express y su version.
app.disable('x-powered-by');
// Habilitamos el modo estricto de rutas ya que por defecto esta deshabilitado. app.enable('strict routing');
// Rutas
const api = "/api";
// Ruta para el apartado de arte.
app.use(`${api}/art`, artRouter({ CinemaModel, MusicModel, PaintingModel }));
// Ruta para el apartado de autenticacion.
app.use(`${api}/auth`, authRouter({ AuthModel }));
// Ruta para el apartado de comida.
app.use(`${api}/food`, foodRouter({ FoodModel }));
// Ruta para el apartado de salud.
app.use(`${api}/health`, healthRouter({ MedicamentModel, MedicalAppointmentModel }));
// Ruta para el apartado de citas.
app.use(`${api}/meeting`, meetingRouter({ MeetingModel }));
// Ruta para el apartado de deportes.
app.use(`${api}/sport`, sportRouter({ SportModel }));
// Ruta para el apartado de viajes.
app.use(`${api}/travel`, travelRouter({ TravelModel }));
// Ruta para el apartado de trabajo.
app.use(`${api}/work`, workRouter({ WorkModel }));
// En cualquier caso devolvemos 404 al no encontrar la ruta especificada.
app.use((req, res) => {
    res.status(404).send({ message: "Recurso no encontrado" });
});