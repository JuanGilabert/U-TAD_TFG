// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateFood, validatePartialFood } from '../../models/food/foodModelValidator.mjs';
// Importamos los valores de las rutas/endpoints
import { FOOD_RESTAURANT_ROUTE_PATH, FOOD_SHOPPING_LIST_ROUTE_PATH,
    IDENTIFIER_ROUTE_PATH, EVENT_ROUTE_PATH, ITEM_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
//// Exportamos el middleware.
export async function foodRequestMiddleware(req, res, next) {
    // Validamos las queries de la peticion.
    if (req.method === 'GET' && Object.keys(req.query).length > 0) {}
    // Creamos la variable que contendra el resultado de la validacion de zod de los datos recibidos en la peticion.
    let zodValidationResult = "";
    // Validamos el cuerpo de la peticion.
    if (req.method === 'POST') {
        // En caso de que la ruta sea /restaurant/event/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateFood(req.body);
        }
        // En caso de que la ruta sea /shopping-list/item/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}/`) {}
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PUT') {
        // En caso de que la ruta sea /restaurant/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateFood(req.body);
        }
        // En caso de que la ruta sea /shopping-list/item/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_SHOPPING_LIST_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {}
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PATCH') {
        // En caso de que la ruta sea /restaurant/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_RESTAURANT_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialFood(req.body);
        }
        // En caso de que la ruta sea /food/shopping-list/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${FOOD_SHOPPING_LIST_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {}
    }
    if (zodValidationResult.error) return res.status(422).json({ message: zodValidationResult.error.message });
    // Verificamos que no haya queries en la peticion.
    if (req.method === 'DELETE' && Object.keys(req.query).length > 0)
        return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Continuamos con el siguiente handler en la cola.
    next();
}