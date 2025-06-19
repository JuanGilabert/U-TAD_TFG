// Modulos de node.
import express from "express";
// Importamos los middlewares necesarios.
import { corsMiddleware } from "./middlewares/corsMiddleware.mjs";
// Importamos las rutas para usarlas y redirigir las peticiones.
import { adminRouter } from "./routes/auth/adminRouter.mjs";
import { userRouter } from "./routes/user/userRouter.mjs";////
import { artRouter } from "./routes/art/artRouter.mjs";
import { foodRouter } from "./routes/food/foodRouter.mjs";
import { healthRouter } from "./routes/health/healthRouter.mjs";
import { meetingRouter } from "./routes/meeting/meetingRouter.mjs";
import { sportRouter } from "./routes/sport/sportRouter.mjs";
import { travelRouter } from "./routes/travel/travelRouter.mjs";
import { workRouter } from "./routes/work/workRouter.mjs";
// Importamos los modelos para inyectarlos.
import { AdminModel } from './models/auth/adminModel.mjs';
import { UserModel } from './models/user/userModel.mjs';////
import { CinemaModel } from './models/art/cinema/cinemaModel.mjs';
import { MusicModel } from './models/art/music/musicModel.mjs';
import { PaintingModel } from './models/art/painting/paintingModel.mjs';
import { FoodModel } from './models/food/foodModel.mjs';
import { MedicamentModel } from './models/health/medicament/medicamentModel.mjs';
import { MedicalAppointmentModel } from './models/health/medicalAppointment/medicalAppointmentModel.mjs';
import { MeetingModel } from './models/meeting/meetingModel.mjs';
import { SportModel } from './models/sport/sportModel.mjs';
import { TravelModel } from './models/travel/travelModel.mjs';
import { WorkModel } from './models/work/workModel.mjs';
// Importamos el modulo de la carpeta utils.
import { commonJSModule } from "./utils/CommonJsModuleImporter.mjs";
//// Inicializamos App Express
export const app = express();
// Middleware para procesar los cuerpos en formato JSON
app.use(express.json());
// Middleware para procesar los datos de formulario.
app.use(express.urlencoded({ extended: true }));
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
const apiServerProcessName = "/api";
//// Administracion
// Ruta para el apartado de administracion.
app.use(`${apiServerProcessName}/admin`, adminRouter({ AdminModel }));
//// Usuarios
// Ruta para el apartado del usuario.
app.use(`${apiServerProcessName}/user`, userRouter({ UserModel }));
// Ruta para el apartado de arte.
app.use(`${apiServerProcessName}/art`, authMiddleware, requestMiddleware, artRouter({ CinemaModel, MusicModel, PaintingModel }));
// Ruta para el apartado de comida.
app.use(`${apiServerProcessName}/food`, foodRouter({ FoodModel }));
// Ruta para el apartado de salud.
app.use(`${apiServerProcessName}/health`, healthRouter({ MedicamentModel, MedicalAppointmentModel }));
// Ruta para el apartado de citas.
app.use(`${apiServerProcessName}/meeting`, meetingRouter({ MeetingModel }));
// Ruta para el apartado de deportes.
app.use(`${apiServerProcessName}/sport`, sportRouter({ SportModel }));
// Ruta para el apartado de viajes.
app.use(`${apiServerProcessName}/travel`, travelRouter({ TravelModel }));
// Ruta para el apartado de trabajo.
app.use(`${apiServerProcessName}/work`, workRouter({ WorkModel }));
// En cualquier caso devolvemos 404 al no encontrar la ruta especificada.
app.use((req, res) => {
    res.status(404).send({ message: "Recurso no encontrado. Revise el enlace del recurso solicitado." });
});