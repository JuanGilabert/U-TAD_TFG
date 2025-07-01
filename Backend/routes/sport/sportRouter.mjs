// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores
import { SportController } from '../../controllers/sport/sportController.mjs';
// Importamos los valores de las rutas/endpoints.
import {
    SPORT_ACTIVITY_ROUTE_PATH, SPORT_ROUTINE_ROUTE_PATH,
    IDENTIFIER_ROUTE_PATH, UNAVAILABLE_DATES_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
/**
 * Creates and returns an Express router for handling sport-related API endpoints.
 * 
 * The router includes the following endpoints:
 * - GET /: Retrieves a list of all sports.
 * - GET /:id: Retrieves a specific sport by ID.
 * - POST /: Adds a new sport entry.
 * - PUT /:id: s an existing sport entry by ID.
 * - PATCH /:id: Partially s an existing sport entry by ID.
 * - DELETE /:id: Deletes a sport entry by ID.
 * 
 * All endpoints use a request middleware for validation or preprocessing.
 * 
 * @param {Object} param - Objeto que contiene el modelo de deporte(SportModel).
 * @param {Object} param.SportModel - El modelo usado para realizar las operaciones.
 * @returns {Router} Un Router de Express para los endpoints relacionados con deportes.
 */
export const sportRouter = ({ SportModel }) => {
    const sportRouter = expressRouterGenerator();
    const sportController = new SportController({ model: SportModel });
    /* Sport-Activity */
    // GET /api/sport/activity
    sportRouter.get(`${SPORT_ACTIVITY_ROUTE_PATH}`, sportController.getAllSports);
    // GET-UNAVAILABLE-DATES --> /api/sport/activity/unavailable-dates
    sportRouter.get(`${SPORT_ACTIVITY_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`, sportController.getSportUnavailableDates);
    // GET-ID /api/sport/activity/:id
    sportRouter.get(`${SPORT_ACTIVITY_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.getSportById);
    // POST 
    sportRouter.post(`${SPORT_ACTIVITY_ROUTE_PATH}`, sportController.postSport);
    // PUT
    sportRouter.put(`${SPORT_ACTIVITY_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.putSport);
    // PATCH
    sportRouter.patch(`${SPORT_ACTIVITY_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.patchSport);
    // DELETE
    sportRouter.delete(`${SPORT_ACTIVITY_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.deleteSport);
    /* Sport-Routine */
    // GET /api/sport/routine
    //sportRouter.get(`${SPORT_ROUTINE_ROUTE_PATH}`, sportController.getAllRoutines);
    // GET-ID /api/sport/routine/:id
    //sportRouter.get(`${SPORT_ROUTINE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.getRoutineById);
    // POST 
    //sportRouter.post(`${SPORT_ROUTINE_ROUTE_PATH}`, sportController.postRoutine);
    // PUT
    //sportRouter.put(`${SPORT_ROUTINE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.putRoutine);
    // PATCH
    //sportRouter.patch(`${SPORT_ROUTINE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.patchRoutine);
    // DELETE
    //sportRouter.delete(`${SPORT_ROUTINE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, sportController.deleteRoutine);
    // Devolvemos la configuración del router.
    return sportRouter;
}
