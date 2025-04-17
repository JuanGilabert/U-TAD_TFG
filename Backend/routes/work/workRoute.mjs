import { Router } from 'express';
// Importamos el middleware de autenticacion y el controlador.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { WorkController } from '../../controllers/work/workController.mjs';
//
export const workRouter = ({ model }) => {
    const workRouter = Router();
    const workController = new WorkController({ model });
    /* Work */
    const ednpointName = "/";
    const identifier = "/:id";
    // GET api/work/
    workRouter.get(`${ednpointName}`, [authMiddleware], workController.getAllWorks);
    // GET-ID api/work/:id
    workRouter.get(`${identifier}`, [authMiddleware], workController.getWorkById);
    // POST api/work/
    workRouter.post(`${ednpointName}`, [authMiddleware], workController.postNewWork);
    // PUT
    workRouter.put(`${identifier}`, [authMiddleware], workController.putUpdateWork);
    // PATCH
    workRouter.patch(`${identifier}`, [authMiddleware], workController.patchUpdateWork);
    // DELETE
    workRouter.delete(`${identifier}`, [authMiddleware], workController.deleteWork);
    //
    return workRouter;
}
