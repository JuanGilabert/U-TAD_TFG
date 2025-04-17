import { Router } from 'express';
// Importamos el middleware de autenticacion y el controlador.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { MeetingController } from '../../controllers/meeting/meetingController.mjs';
//
export const meetingRouter = ({ model }) => {
    const meetingRouter = Router();
    const meetingController = new MeetingController({ model });
    /* Meeting */
    const ednpointName = "/";
    const identifier = "/:id";
    // GET api/meeting/
    meetingRouter.get(`${ednpointName}`, [authMiddleware], meetingController.getAllMeetings);
    // GET-ID api/meeting/:id
    meetingRouter.get(`${identifier}`, [authMiddleware], meetingController.getMeetingById);
    // POST api/meeting/
    meetingRouter.post(`${ednpointName}`, [authMiddleware], meetingController.postNewMeeting);
    // PUT
    meetingRouter.put(`${identifier}`, [authMiddleware], meetingController.putUpdateMeeting);
    // PATCH
    meetingRouter.patch(`${identifier}`, [authMiddleware], meetingController.patchUpdateMeeting);
    // DELETE
    meetingRouter.delete(`${identifier}`, [authMiddleware], meetingController.deleteMeeting);
    //
    return meetingRouter;
}
