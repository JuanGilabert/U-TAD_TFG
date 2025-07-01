// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores necesarios.
import { HealthMedicamentController } from '../../controllers/health/medicamentController.mjs';
import { HealthMedicalAppointmentController } from '../../controllers/health/medicalAppointmentController.mjs';
// Importamos los valores de las rutas/endpoints.
import {
    HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH, HEALTH_MEDICAMENT_ROUTE_PATH,
    MEDICAMENT_EXPIRATION_DATE_ROUTE_PATH, IDENTIFIER_ROUTE_PATH, ITEM_ROUTE_PATH, APPOINTMENT_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
/**
 * Configures the health-related routes for the application.
 * 
 * This router handles endpoints related to medicaments and medical appointments,
 * providing functionalities for retrieving, creating, updating, and deleting
 * resources in these categories. It uses controllers to process the requests and
 * return appropriate responses.
 * 
 * @param {Object} params - The parameters for configuring the router.
 * @param {Object} params.MedicamentModel - The model for medicaments.
 * @param {Object} params.MedicalAppointmentModel - The model for medical appointments.
 * 
 * @returns {Router} The configured health router.
 */
export const healthRouter = ({ MedicamentModel, MedicalAppointmentModel }) => {
    const healthRouter = expressRouterGenerator();
    /* Medicament */
    const healthMedicamentController = new HealthMedicamentController({ model: MedicamentModel });
    // GET /api/health/medicament/item
    healthRouter.get(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}`,
        healthMedicamentController.getAllMedicaments
    );
    // GET-UNAVAILABLE-DATES --> /api/health/medicament/expiration-dates
    healthRouter.get(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${MEDICAMENT_EXPIRATION_DATE_ROUTE_PATH}`,
        healthMedicamentController.getMedicamentExpirationDates
    );
    // GET_BY_ID api/health/medicament/item/:id
    healthRouter.get(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicamentController.getMedicamentById
    );
    // POST
    healthRouter.post(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}`,
        healthMedicamentController.postMedicament
    );
    // PUT
    healthRouter.put(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicamentController.putMedicament
    );
    // PATCH
    healthRouter.patch(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicamentController.patchMedicament
    );
    // DELETE
    healthRouter.delete(
        `${HEALTH_MEDICAMENT_ROUTE_PATH}${ITEM_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicamentController.deleteMedicament
    );
    /* Medical Appointments */
    const healthMedicalAppointmentController = new HealthMedicalAppointmentController({ model: MedicalAppointmentModel });
    // GET /api/health/medical-appointment/appointment
    healthRouter.get(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}`,
        healthMedicalAppointmentController.getAllMedicalAppointments
    );
    // GET-UNAVAILABLE-DATES --> /api/health/medical-appointment/unavailable-dates
    healthRouter.get(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`,
        healthMedicalAppointmentController.getMedicalAppointmentUnavailableDates
    );
    // GET_APPOINTMENT_BY_ID /api/health/medical-appointment/appointment/:id
    healthRouter.get(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicalAppointmentController.getMedicalAppointmentById
    );
    // POST
    healthRouter.post(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}`,
        healthMedicalAppointmentController.postMedicalAppointment
    );
    // PUT
    healthRouter.put(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicalAppointmentController.putMedicalAppointment
    );
    // PATCH
    healthRouter.patch(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicalAppointmentController.patchMedicalAppointment
    );
    // DELETE
    healthRouter.delete(
        `${HEALTH_MEDICAL_APPOINTMENT_ROUTE_PATH}${APPOINTMENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        healthMedicalAppointmentController.deleteMedicalAppointment
    );
    // Devolvemos la configuración del router.
    return healthRouter;
}