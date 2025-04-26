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
    /* Medicament */
    const medicamentEndpoint = '/medicament';
    const healthMedicamentController = new HealthMedicamentController({ model: MedicamentModel });
    // GET. api/health/medicament
    healthRouter.get(`${medicamentEndpoint}`, [requestMiddleware], healthMedicamentController.getAllMedicaments);
    // GET_BY_ID api/health/medicament/:id
    healthRouter.get(`${medicamentEndpoint}${identifier}`, [requestMiddleware], healthMedicamentController.getMedicamentById);
    // POST
    healthRouter.post(`${medicamentEndpoint}`, [requestMiddleware], healthMedicamentController.postNewMedicament);
    // PUT
    healthRouter.put(`${medicamentEndpoint}${identifier}`, [requestMiddleware], healthMedicamentController.putUpdateMedicament);
    // PATCH
    healthRouter.patch(`${medicamentEndpoint}${identifier}`, [requestMiddleware], healthMedicamentController.patchUpdateMedicament);
    // DELETE
    healthRouter.delete(`${medicamentEndpoint}${identifier}`, [requestMiddleware], healthMedicamentController.deleteMedicament);
    /* MedicalAppointments */
    const medicalAppointmentEndpoint = '/medical_appointment';
    const healthMedicalAppointmentController = new HealthMedicalAppointmentController({ model: MedicalAppointmentModel });
    // GET api/health/medical_appointment
    healthRouter.get(`${medicalAppointmentEndpoint}`, [requestMiddleware], healthMedicalAppointmentController.getAllMedicalAppointments);
    // GET_APPOINTMENT_BY_ID api/health/medical_appointment/:id
    healthRouter.get(`${medicalAppointmentEndpoint}${identifier}`, [requestMiddleware], healthMedicalAppointmentController.getMedicalAppointmentById);
    // POST
    healthRouter.post(`${medicalAppointmentEndpoint}`, [requestMiddleware], healthMedicalAppointmentController.postNewMedicalAppointment);
    // PUT
    healthRouter.put(`${medicalAppointmentEndpoint}${identifier}`, [requestMiddleware], healthMedicalAppointmentController.putUpdateMedicalAppointment);
    // PATCH
    healthRouter.patch(`${medicalAppointmentEndpoint}${identifier}`, [requestMiddleware], healthMedicalAppointmentController.patchUpdateMedicalAppointment);
    // DELETE
    healthRouter.delete(`${medicalAppointmentEndpoint}${identifier}`, [requestMiddleware], healthMedicalAppointmentController.deleteMedicalAppointment);
    //
    return healthRouter;
}