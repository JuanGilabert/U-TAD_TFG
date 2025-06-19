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
 * - PUT /:id: s an existing sport entry by ID.
 * - PATCH /:id: Partially s an existing sport entry by ID.
 * - DELETE /:id: Deletes a sport entry by ID.
 * 
 * All endpoints use a request middleware for validation or preprocessing.
 * 
 * @param {Object} param - Objeto que contiene el modelo de deporte(SportModel).
 * @param {Object} param.SportModel - El modelo usado para realizar las operaciones.
 * @returns {Router} Un Router de Express para los endpoints relacionados con deportes.
 */
export const sportRouter = ({ SportModel }) => {
    const sportRouter = Router();
    const sportController = new SportController({ model: SportModel });
    /* Sport */
    const ednpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET api/sport/
    sportRouter.get(`${ednpointName}`, [authMiddleware], [requestMiddleware], sportController.getAllSports);
    // GET-UNAVAILABLE-DATES --> /api/sport/unavailable-dates
    sportRouter.get(`${unavailableDates}`, [authMiddleware], [requestMiddleware], sportController.getSportUnavailableDates);
    // GET-ID api/sport/:id
    sportRouter.get(`${identifier}`, [authMiddleware], [requestMiddleware], sportController.getSportById);
    // POST 
    sportRouter.post(`${ednpointName}`, [authMiddleware], [requestMiddleware], sportController.postSport);
    // PUT
    sportRouter.put(`${identifier}`, [authMiddleware], [requestMiddleware], sportController.putSport);
    // PATCH
    sportRouter.patch(`${identifier}`, [authMiddleware], [requestMiddleware], sportController.patchSport);
    // DELETE
    sportRouter.delete(`${identifier}`, [authMiddleware], [requestMiddleware], sportController.deleteSport);
    // DEvolvemos la configuración del router.
    return sportRouter;
}
