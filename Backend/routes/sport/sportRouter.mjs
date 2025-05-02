// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { SportController } from '../../controllers/sports/sportController.mjs';
/**
 * Creates and returns an Express router for handling sport-related API endpoints.
 * 
 * The router includes the following endpoints:
 * - GET /: Retrieves a list of all sports.
 * - GET /:id: Retrieves a specific sport by ID.
 * - POST /: Adds a new sport entry.
 * - PUT /:id: Updates an existing sport entry by ID.
 * - PATCH /:id: Partially updates an existing sport entry by ID.
 * - DELETE /:id: Deletes a sport entry by ID.
 * 
 * All endpoints use a request middleware for validation or preprocessing.
 * 
 * @param {Object} param - An object containing the SportModel.
 * @param {Object} param.SportModel - The model used for sport data operations.
 * @returns {Router} An Express router for sport-related endpoints.
 */
export const sportRouter = ({ SportModel }) => {
    const sportRouter = Router();
    const sportController = new SportController({ model: SportModel });
    /* Sport */
    const ednpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET api/sport/
    sportRouter.get(`${ednpointName}`, [requestMiddleware], sportController.getAllSports);
    // GET-UNAVAILABLE-DATES --> /api/sport/unavailable-dates
    sportRouter.get(`${unavailableDates}`, [requestMiddleware], sportController.getSportUnavailableDates);
    // GET-ID api/sport/:id
    sportRouter.get(`${identifier}`, [requestMiddleware], sportController.getSportById);
    // POST 
    sportRouter.post(`${ednpointName}`, [requestMiddleware], sportController.postNewSport);
    // PUT
    sportRouter.put(`${identifier}`, [requestMiddleware], sportController.putUpdateSport);
    // PATCH
    sportRouter.patch(`${identifier}`, [requestMiddleware], sportController.patchUpdateSport);
    // DELETE
    sportRouter.delete(`${identifier}`, [requestMiddleware], sportController.deleteSport);
    // DEvolvemos la configuración del router.
    return sportRouter;
}
