import { Router } from 'express';
// Importamos el middleware de autenticacion y el controlador.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { TravelController } from '../../controllers/travel/travelController.mjs';
//
export const travelRouter = ({ model }) => {
    const travelRouter = Router();
    const travelController = new TravelController({ model })
    /* Travel */
    const ednpointName = "/";
    const identifier = "/:id";
    // GET api/travel/
    travelRouter.get(`${ednpointName}`, [authMiddleware], travelController.getAllTravels);
    // GET-ID api/travel/:id
    travelRouter.get(`${identifier}`, [authMiddleware], travelController.getTravelById);
    // POST api/travel/
    travelRouter.post(`${ednpointName}`, [authMiddleware], travelController.postNewTravel);
    // PUT
    travelRouter.put(`${identifier}`, [authMiddleware], travelController.putUpdateTravel);
    // PATCH
    travelRouter.patch(`${identifier}`, [authMiddleware], travelController.patchUpdateTravel);
    // DELETE
    travelRouter.delete(`${identifier}`, [authMiddleware], travelController.deleteTravel);
    //
    return travelRouter;
}