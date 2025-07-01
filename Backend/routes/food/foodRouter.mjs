// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores necesarios.
import { FoodController } from '../../controllers/food/foodController.mjs';
// Importamos los valores de las rutas/endpoints.
import { FOOD_RESTAURANT_ROUTE_PATH, FOOD_SHOPPING_LIST_ROUTE_PATH, EVENT_ROUTE_PATH,
    IDENTIFIER_ROUTE_PATH, UNAVAILABLE_DATES_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
/** foodRouter
 * Configures and returns an Express Router for the food-related API endpoints.
 * 
 * @param {Object} dependencies - An object containing dependencies for the router.
 * @param {Object} dependencies.FoodModel - The model used by the FoodController.
 * @returns {Router} An Express Router configured with routes for food operations.
 */
export const foodRouter = ({ FoodModel }) => {
    const foodRouter = expressRouterGenerator();
    const foodController = new FoodController({ model: FoodModel });
    /* Restaurant */
    // GET --> /api/food/restaurant/event
    foodRouter.get(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}`,
        foodController.getAllFoods
    );
    // GET-UNAVAILABLE-DATES --> /api/food/restaurant/event/unavailable-dates
    foodRouter.get(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`,
        foodController.getFoodUnavailableDates
    );
    // GET-ID --> /api/food/restaurant/event/:id
    foodRouter.get(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        foodController.getFoodById
    );
    // POST
    foodRouter.post(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}`,
        foodController.postFood
    );
    // PUT
    foodRouter.put(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        foodController.putFood
    );
    // PATCH
    foodRouter.patch(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        foodController.patchFood
    );
    // DELETE
    foodRouter.delete(
        `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        foodController.deleteFood
    );
    /* Shopping List */
    // GET --> /api/food/shopping-list/item
    //foodRouter.get(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}`, foodController.getAllShoppingListItems);
    // GET-ID --> /api/food/shopping-list/item/:id
    //foodRouter.get(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, foodController.getShoppingListItemById);
    // POST
    //foodRouter.post(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}`, foodController.postShoppingListItem);
    // PUT
    //foodRouter.put(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, foodController.putShoppingListItem);
    // PATCH
    //foodRouter.patch(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, foodController.patchShoppingListItem);
    // DELETE
    //foodRouter.delete(
    // `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, foodController.deleteShoppingListItem);
    // Devolvemos la configuración del router.
    return foodRouter;
}