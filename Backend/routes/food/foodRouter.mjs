// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { FoodController } from '../../controllers/food/foodController.mjs';
/**
 * Configures and returns an Express Router for the food-related API endpoints.
 * 
 * @param {Object} dependencies - An object containing dependencies for the router.
 * @param {Object} dependencies.FoodModel - The model used by the FoodController.
 * @returns {Router} An Express Router configured with routes for food operations.
 * 
 * Routes:
 * - GET /api/food/ : Retrieves all food items for a user.
 * - GET /api/food/:id : Retrieves a specific food item by ID for a user.
 * - POST /api/food/ : Adds a new food item for a user.
 * - PUT /api/food/:id : Updates an existing food item by replacing it.
 * - PATCH /api/food/:id : Partially updates an existing food item.
 * - DELETE /api/food/:id : Deletes a specific food item by ID.
 * 
 * Middleware:
 * - requestMiddleware: Applied to all routes to process requests.
 */
export const foodRouter = ({ FoodModel }) => {
    const foodRouter = Router();
    const foodController = new FoodController({ model: FoodModel });
    /* Food */
    const endpointName = "/";
    const identifier = "/:id";
    // GET --> /api/food/
    foodRouter.get(`${endpointName}`, [requestMiddleware], foodController.getAllFoods);
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
    //
    return foodRouter;
}