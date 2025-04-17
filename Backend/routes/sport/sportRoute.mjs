import { Router } from 'express';
// Importamos el middleware de autenticacion y  el controlador.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { SportController } from '../../controllers/sports/sportController.mjs';
//
export const sportRouter = ({ model }) => {
    const sportRouter = Router();
    const sportController = new SportController({ model });
    /* Sport */
    const ednpointName = "/";
    const identifier = "/:id";
    // GET api/sport/
    sportRouter.get(`${ednpointName}`, [authMiddleware], sportController.getAllSports);
    // GET-ID api/sport/:id
    sportRouter.get(`${identifier}`, [authMiddleware], sportController.getSportById);
    // POST 
    sportRouter.post(`${ednpointName}`, [authMiddleware], sportController.postNewSport);
    // PUT
    sportRouter.put(`${identifier}`, [authMiddleware], sportController.putUpdateSport);
    // PATCH
    sportRouter.patch(`${identifier}`, [authMiddleware], sportController.patchUpdateSport);
    // DELETE
    sportRouter.delete(`${identifier}`, [authMiddleware], sportController.deleteSport);
    //
    return sportRouter;
}
