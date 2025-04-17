// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de autenticacion y el controlador.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { FoodController } from '../../controllers/food/foodController.mjs';
//
export const foodRouter = ({ model }) => {
    const foodRouter = Router();
    const foodController = new FoodController({ model });
    /* Food */
    const endpointName = "/";
    const identifier = "/:id";
    // GET --> /api/food/
    foodRouter.get(`${endpointName}`, [authMiddleware], foodController.getAllFoods);
    // GET-ID --> /api/food/:id
    foodRouter.get(`${identifier}`, [authMiddleware], foodController.getFoodById);
    // POST
    foodRouter.post(`${endpointName}`, [authMiddleware], foodController.postNewFood);
    // PUT
    foodRouter.put(`${identifier}`, [authMiddleware], foodController.putUpdateFood);
    // PATCH
    foodRouter.patch(`${identifier}`, [authMiddleware], foodController.patchUpdateFood);
    // DELETE
    foodRouter.delete(`${identifier}`, [authMiddleware], foodController.deleteFood);
    //
    return foodRouter;
}