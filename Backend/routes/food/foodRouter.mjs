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
    foodRouter.get(`${endpointName}`, [requestMiddleware], foodController.getAllFoods);
    // GET-UNAVAILABLE-DATES --> /api/food/unavailable-dates
    foodRouter.get(`${unavailableDates}`, [requestMiddleware], foodController.getFoodUnavailableDates);
    // GET-ID --> /api/food/:id
    foodRouter.get(`${identifier}`, [requestMiddleware], foodController.getFoodById);
    // POST
    foodRouter.post(`${endpointName}`, [requestMiddleware], foodController.postNewFood);
    // PUT
    foodRouter.put(`${identifier}`, [requestMiddleware], foodController.putUpdateFood);
    // PATCH
    foodRouter.patch(`${identifier}`, [requestMiddleware], foodController.patchUpdateFood);
    // DELETE
    foodRouter.delete(`${identifier}`, [requestMiddleware], foodController.deleteFood);
    // Devolvemos la configuración del router.
    return foodRouter;
}