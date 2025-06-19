// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { FoodController } from '../../controllers/food/foodController.mjs';
/** foodRouter
 * Configures and returns an Express Router for the food-related API endpoints.
 * 
 * @param {Object} dependencies - An object containing dependencies for the router.
 * @param {Object} dependencies.FoodModel - The model used by the FoodController.
 * @returns {Router} An Express Router configured with routes for food operations.
 */
export const foodRouter = ({ FoodModel }) => {
    const foodRouter = Router();
    const foodController = new FoodController({ model: FoodModel });
    /* Food */
    const endpointName = "/";
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    // GET --> /api/food/
    foodRouter.get(`${endpointName}`, [authMiddleware], [requestMiddleware], foodController.getAllFoods);
    // GET-UNAVAILABLE-DATES --> /api/food/unavailable-dates
    foodRouter.get(`${unavailableDates}`, [authMiddleware], [requestMiddleware], foodController.getFoodUnavailableDates);
    // GET-ID --> /api/food/:id
    foodRouter.get(`${identifier}`, [authMiddleware], [requestMiddleware], foodController.getFoodById);
    // POST
    foodRouter.post(`${endpointName}`, [authMiddleware], [requestMiddleware], foodController.postFood);
    // PUT
    foodRouter.put(`${identifier}`, [authMiddleware], [requestMiddleware], foodController.putFood);
    // PATCH
    foodRouter.patch(`${identifier}`, [authMiddleware], [requestMiddleware], foodController.patchFood);
    // DELETE
    foodRouter.delete(`${identifier}`, [authMiddleware], [requestMiddleware], foodController.deleteFood);
    // Devolvemos la configuración del router.
    return foodRouter;
}