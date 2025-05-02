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
    workRouter.get(`${ednpointName}`, [requestMiddleware], workController.getAllWorks);
    // GET-UNAVAILABLE-DATES --> /api/work/unavailable-dates
    workRouter.get(`${unavailableDates}`, [requestMiddleware], workController.getWorkUnavailableDates);
    // GET-ID api/work/:id
    workRouter.get(`${identifier}`, [requestMiddleware], workController.getWorkById);
    // POST api/work/
    workRouter.post(`${ednpointName}`, [requestMiddleware], workController.postNewWork);
    // PUT
    workRouter.put(`${identifier}`, [requestMiddleware], workController.putUpdateWork);
    // PATCH
    workRouter.patch(`${identifier}`, [requestMiddleware], workController.patchUpdateWork);
    // DELETE
    workRouter.delete(`${identifier}`, [requestMiddleware], workController.deleteWork);
    //
    return workRouter;
}
