// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateUser, validatePartialUser } from '../../models/user/userModelValidator.mjs';
// Importamos los valores de las rutas/endpoints.
import { API_SERVER_PROCESS_NAME, USER_ROUTE_PATH, IDENTIFIER_ROUTE_PATH } from '../../config/GenericEnvConfig.mjs';
//// Exportamos el middleware.
export async function userRequestMiddleware(req, res, next) {
    // Validamos las queries de la peticion.
    if (req.method === 'GET' && Object.keys(req.query).length > 0) {}
    // Creamos la variable que contendra el resultado de la validacion de zod de los datos recibidos en la peticion.
    let zodValidationResult = "";
    // Validamos el cuerpo de la peticion.
    if (req.method === 'POST') {
        // En caso de que la ruta sea /api/user/ evaluamos el cuerpo de la peticion en concreto.
        if (req.baseUrl == `${API_SERVER_PROCESS_NAME}${USER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateUser(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PUT') {
        // En caso de que la ruta sea /api/user/:id/ evaluamos el cuerpo de la peticion en concreto.
        if (req.baseUrl == `${API_SERVER_PROCESS_NAME}${USER_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateUser(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PATCH') {
        // En caso de que la ruta sea /api/user/:id/ evaluamos el cuerpo de la peticion en concreto.
        if (req.baseUrl == `${API_SERVER_PROCESS_NAME}${USER_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialUser(req.body);
        }
    }
    if (zodValidationResult.error) return res.status(422).json({ message: zodValidationResult.error.message });
    // VALIDACION DEL DELETE DEL USER CON ?soft=true para solo cambiar el valor de active a false.
    if (req.method === 'DELETE' && Object.keys(req.query).length > 0) {
        // En caso de que la ruta sea /api/user/:id/ evaluamos la query de la peticion en concreto.
        if (req.baseUrl == `${API_SERVER_PROCESS_NAME}${USER_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {}
    }
    // Continuamos con el siguiente handler en la cola.
    next();
}