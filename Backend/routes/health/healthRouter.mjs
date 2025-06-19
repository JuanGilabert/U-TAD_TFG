// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { HealthMedicamentController } from '../../controllers/health/healthMedicamentController.mjs';
import { HealthMedicalAppointmentController } from '../../controllers/health/healthMedicalAppointmentController.mjs';
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
    const healthRouter = Router();
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    /* Medicament */
    const medicamentEndpoint = '/medicament';
    const healthMedicamentController = new HealthMedicamentController({ model: MedicamentModel });
    // GET. api/health/medicament
    healthRouter.get(`${medicamentEndpoint}`, [authMiddleware], [requestMiddleware], healthMedicamentController.getAllMedicaments);
    // GET-UNAVAILABLE-DATES --> /api/health/medicament/expiration-dates/
    healthRouter.get(`${medicamentEndpoint}/expiration-dates/`, [authMiddleware], [requestMiddleware], healthMedicamentController.getMedicamentExpirationDates);
    // GET_BY_ID api/health/medicament/:id
    healthRouter.get(`${medicamentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicamentController.getMedicamentById);
    // POST
    healthRouter.post(`${medicamentEndpoint}`, [authMiddleware], [requestMiddleware], healthMedicamentController.postMedicament);
    // PUT
    healthRouter.put(`${medicamentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicamentController.putMedicament);
    // PATCH
    healthRouter.patch(`${medicamentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicamentController.patchMedicament);
    // DELETE
    healthRouter.delete(`${medicamentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicamentController.deleteMedicament);
    /* MedicalAppointments */
    const medicalAppointmentEndpoint = '/medical-appointment';
    const healthMedicalAppointmentController = new HealthMedicalAppointmentController({ model: MedicalAppointmentModel });
    // GET api/health/medical-appointment
    healthRouter.get(`${medicalAppointmentEndpoint}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.getAllMedicalAppointments);
    // GET-UNAVAILABLE-DATES --> /api/health/medical-appointment/unavailable-dates
    healthRouter.get(`${medicalAppointmentEndpoint}${unavailableDates}`, [authMiddleware], [requestMiddleware],healthMedicalAppointmentController.getMedicalAppointmentUnavailableDates);
    // GET_APPOINTMENT_BY_ID api/health/medical_appointment/:id
    healthRouter.get(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.getMedicalAppointmentById);
    // POST
    healthRouter.post(`${medicalAppointmentEndpoint}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.postMedicalAppointment);
    // PUT
    healthRouter.put(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.putMedicalAppointment);
    // PATCH
    healthRouter.patch(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.patchMedicalAppointment);
    // DELETE
    healthRouter.delete(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], [requestMiddleware], healthMedicalAppointmentController.deleteMedicalAppointment);
    // Devolvemos la configuración del router.
    return healthRouter;
}