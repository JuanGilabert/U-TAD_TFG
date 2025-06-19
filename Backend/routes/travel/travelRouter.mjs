// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { TravelController } from '../../controllers/travel/travelController.mjs';
/**
 * Creates and returns a router for handling travel-related API endpoints.
 *
 * The router provides the following endpoints:
 * - GET /api/travel/: Retrieves all travel records.
 * - GET /api/travel/:id: Retrieves a travel record by its unique identifier.
 * - POST /api/travel/: Creates a new travel record.
 * - PUT /api/travel/:id: s an entire travel record by its unique identifier.
 * - PATCH /api/travel/:id: s parts of a travel record by its unique identifier.
 * - DELETE /api/travel/:id: Deletes a travel record by its unique identifier.
 *
 * @param {Object} param0 - An object containing the TravelModel.
 * @returns {Router} An Express Router configured with travel endpoints.
 */
export const travelRouter = ({ TravelModel }) => {
    const travelRouter = Router();
    const travelController = new TravelController({ model: TravelModel });
    /* Travel */
    const ednpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET api/travel/
    travelRouter.get(`${ednpointName}`, [authMiddleware], [requestMiddleware], travelController.getAllTravels);
    // GET-UNAVAILABLE-DATES --> /api/travel/unavailable-dates
    travelRouter.get(`${unavailableDates}`, [authMiddleware], [requestMiddleware], travelController.getTravelDates);
    // GET-ID api/travel/:id
    travelRouter.get(`${identifier}`, [authMiddleware], [requestMiddleware], travelController.getTravelById);
    // POST api/travel/
    travelRouter.post(`${ednpointName}`, [authMiddleware], [requestMiddleware], travelController.postTravel);
    // PUT
    travelRouter.put(`${identifier}`, [authMiddleware], [requestMiddleware], travelController.putTravel);
    // PATCH
    travelRouter.patch(`${identifier}`, [authMiddleware], [requestMiddleware], travelController.patchTravel);
    // DELETE
    travelRouter.delete(`${identifier}`, [authMiddleware], [requestMiddleware], travelController.deleteTravel);
    // DEvolvemos la configuración del router.
    return travelRouter;
}