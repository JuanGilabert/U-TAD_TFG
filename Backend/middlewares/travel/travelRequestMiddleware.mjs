// Importamos los modelos/schemas para validar los datos de las peticiones recibidos en este middleware.
import { validateTravel, validatePartialTravel } from '../../models/travel/travelModelValidator.mjs';
// Importamos los valores de las rutas/endpoints necesarios para este middleware.
import { TRAVEL_TRIP_ROUTE_PATH, IDENTIFIER_ROUTE_PATH } from '../../config/GenericEnvConfig.mjs';
//// Exportamos el middleware.
export async function travelRequestMiddleware(req, res, next) {
    // Validamos las queries de la peticion.
    if (req.method === 'GET' && Object.keys(req.query).length > 0) {}
    // Creamos la variable que contendra el resultado de la validacion de zod de los datos recibidos en la peticion.
    let zodValidationResult = "";
    // Validamos el cuerpo de la peticion.
    if (req.method === 'POST') {
        // En caso de que la ruta sea /trip/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${TRAVEL_TRIP_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateTravel(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PUT') {
        // En caso de que la ruta sea /trip/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateTravel(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PATCH') {
        // En caso de que la ruta sea /trip/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialTravel(req.body);
        }
    }
    if (zodValidationResult.error) return res.status(422).json({ message: zodValidationResult.error.message });
    // Verificamos que no haya queries en la peticion.
    if (req.method === 'DELETE' && Object.keys(req.query).length > 0)
        return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Continuamos con el siguiente handler en la cola.
    next();
}