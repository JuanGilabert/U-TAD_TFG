// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores
import { MeetingController } from '../../controllers/meeting/meetingController.mjs';
// Importamos los valores de las rutas/endpoints necesarios.
import { EVENT_ROUTE_PATH, IDENTIFIER_ROUTE_PATH, UNAVAILABLE_DATES_ROUTE_PATH } from '../../config/GenericEnvConfig.mjs';
/** meetingRouter.
 * 
 * @param {Object} { MeetingModel } - Objeto que contiene el modelo MeetingModel.
 * @returns {Router} - Router de la ruta /meeting.
 * 
 * La ruta /meeting permite realizar operaciones CRUD sobre la coleccion de Meeting.
 * - GET /meeting/ - Obtiene todos los registros de Meeting.
 * - GET-UNAVAILABLE-DATES - Obtiene las fechas no disponibles de Meeting.
 * - GET /meeting/:id - Obtiene un registro de Meeting por su id.
 * - POST /meeting/ - Crea un registro de Meeting.
 * - PUT /meeting/:id - Actualiza un registro de Meeting.
 * - PATCH /meeting/:id - Actualiza parcialmente un registro de Meeting.
 * - DELETE /meeting/:id - Elimina un registro de Meeting.
 */
export const meetingRouter = ({ MeetingModel }) => {
    const meetingRouter = expressRouterGenerator();
    const meetingController = new MeetingController({ model: MeetingModel });
    /* Meeting */
    // GET /api/meeting/event
    meetingRouter.get(`${EVENT_ROUTE_PATH}`, meetingController.getAllMeetings);
    // GET-UNAVAILABLE-DATES --> /api/meeting/event/unavailable-dates
    meetingRouter.get(
        `${EVENT_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`,
        meetingController.getMeetingUnavailableDates
    );
    // GET-ID /api/meeting/event/:id
    meetingRouter.get(
        `${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        meetingController.getMeetingById)
        ;
    // POST
    meetingRouter.post(`${EVENT_ROUTE_PATH}`, meetingController.postMeeting);
    // PUT
    meetingRouter.put(
        `${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        meetingController.putMeeting
    );
    // PATCH
    meetingRouter.patch(
        `${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        meetingController.patchMeeting
    );
    // DELETE
    meetingRouter.delete(
        `${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        meetingController.deleteMeeting
    );
    // Devolvemos la configuración del router.
    return meetingRouter;
}