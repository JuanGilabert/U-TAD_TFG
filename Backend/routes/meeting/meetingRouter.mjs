// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { MeetingController } from '../../controllers/meeting/meetingController.mjs';
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
    const meetingRouter = Router();
    const meetingController = new MeetingController({ model: MeetingModel });
    /* Meeting */
    const ednpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET api/meeting/
    meetingRouter.get(`${ednpointName}`, [requestMiddleware], meetingController.getAllMeetings);
    // GET-UNAVAILABLE-DATES --> /api/meeting/unavailable-dates
    meetingRouter.get(`${unavailableDates}`, [requestMiddleware], meetingController.getMeetingUnavailableDates);
    // GET-ID api/meeting/:id
    meetingRouter.get(`${identifier}`, [requestMiddleware], meetingController.getMeetingById);
    // POST api/meeting/
    meetingRouter.post(`${ednpointName}`, [requestMiddleware], meetingController.postNewMeeting);
    // PUT
    meetingRouter.put(`${identifier}`, [requestMiddleware], meetingController.putUpdateMeeting);
    // PATCH
    meetingRouter.patch(`${identifier}`, [requestMiddleware], meetingController.patchUpdateMeeting);
    // DELETE
    meetingRouter.delete(`${identifier}`, [requestMiddleware], meetingController.deleteMeeting);
    // Devolvemos la configuración del router.
    return meetingRouter;
}