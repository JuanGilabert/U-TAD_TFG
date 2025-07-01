// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores
import { WorkController } from '../../controllers/work/workController.mjs';
// Importamos los valores de las rutas/endpoints.
import {
    TASK_ROUTE_PATH,
    IDENTIFIER_ROUTE_PATH, UNAVAILABLE_DATES_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
//// Exportamos el router.
export const workRouter = ({ WorkModel }) => {
    const workRouter = expressRouterGenerator();
    const workController = new WorkController({ model: WorkModel });
    /* Work-Task */
    // GET /api/work/task
    workRouter.get(`${TASK_ROUTE_PATH}`, workController.getAllWorks);
    // GET-UNAVAILABLE-DATES --> /api/work/task/unavailable-dates
    workRouter.get(
        `${TASK_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`,
        workController.getWorkUnavailableDates
    );
    // GET-ID /api/work/task/:id
    workRouter.get(
        `${TASK_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        workController.getWorkById
    );
    // POST /api/work/task
    workRouter.post(`${TASK_ROUTE_PATH}`, workController.postWork);
    // PUT
    workRouter.put(
        `${TASK_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        workController.putWork
    );
    // PATCH
    workRouter.patch(
        `${TASK_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        workController.patchWork
    );
    // DELETE
    workRouter.delete(
        `${TASK_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        workController.deleteWork
    );
    // Devlvemos la configuracion del router.
    return workRouter;
}