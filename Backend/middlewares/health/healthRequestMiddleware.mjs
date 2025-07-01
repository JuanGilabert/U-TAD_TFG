// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateMedicament, validatePartialMedicament } from '../../models/health/medicament/medicamentModelValidator.mjs';
import { validateMedicalAppointment, validatePartialMedicalAppointment } from '../../models/health/medicalAppointment/medicalAppointmentModelValidator.mjs';
// Importamos los valores de las rutas/endpoints.
import { HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH, HEALTH_MEDICAMENT_ROUTE_PATH,
    IDENTIFIER_ROUTE_PATH, ITEM_ROUTE_PATH, APPOINTMENT_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
//// Exportamos el middleware.
export async function healthRequestMiddleware(req, res, next) {
    // Validamos las queries de la peticion.
    if (req.method === 'GET' && Object.keys(req.query).length > 0) {}
    // Creamos la variable que contendra el resultado de la validacion de zod de los datos recibidos en la peticion.
    let zodValidationResult = "";
    // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
    if (req.method === 'POST') {
        // En caso de que la ruta sea /medicament/item/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMedicament(req.body);
        }
        // En caso de que la ruta sea /medical-appointment/appointment/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMedicalAppointment(req.body);
        }
    }
    if (req.method === 'PUT') {
        // En caso de que la ruta sea /medicament/item/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMedicament(req.body);
        }
        // En caso de que la ruta sea /medical-appointment/appointment/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMedicalAppointment(req.body);
        }
    }
    if (req.method === 'PATCH') {
        // En caso de que la ruta sea /medicament/item/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialMedicament(req.body);
        }
        // En caso de que la ruta sea /medical-appointment/appointment/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialMedicalAppointment(req.body);
        }
    }
    if (zodValidationResult.error) return res.status(422).json({ message: zodValidationResult.error.message });
    // Verificamos que no haya queries en la peticion.
    if (req.method === 'DELETE' && Object.keys(req.query).length > 0)
        return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Continuamos con el siguiente handler en la cola.
    next();
}