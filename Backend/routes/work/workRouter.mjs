// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { WorkController } from '../../controllers/work/workController.mjs';
export const workRouter = ({ WorkModel }) => {
    const workRouter = Router();
    const workController = new WorkController({ model: WorkModel });
    /* Work */
    const ednpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET api/work/
    workRouter.get(`${ednpointName}`, [authMiddleware], [requestMiddleware], workController.getAllWorks);
    // GET-UNAVAILABLE-DATES --> /api/work/unavailable-dates
    workRouter.get(`${unavailableDates}`, [authMiddleware], [requestMiddleware], workController.getWorkUnavailableDates);
    // GET-ID api/work/:id
    workRouter.get(`${identifier}`, [authMiddleware], [requestMiddleware], workController.getWorkById);
    // POST api/work/
    workRouter.post(`${ednpointName}`, [authMiddleware], [requestMiddleware], workController.postWork);
    // PUT
    workRouter.put(`${identifier}`, [authMiddleware], [requestMiddleware], workController.putWork);
    // PATCH
    workRouter.patch(`${identifier}`, [authMiddleware], [requestMiddleware], workController.patchWork);
    // DELETE
    workRouter.delete(`${identifier}`, [authMiddleware], [requestMiddleware], workController.deleteWork);
    //
    return workRouter;
}
