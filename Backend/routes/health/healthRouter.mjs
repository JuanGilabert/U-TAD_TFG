import { Router } from 'express';
// Importamos el middleware de autenticacion y los controladores
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { HealthMedicamentController } from '../../controllers/health/healthMedicamentController.mjs';
import { HealthMedicalAppointmentController } from '../../controllers/health/healtMedicalAppointmentController.mjs';
////
export const healthRouter = ({ medicamentModel, medicalAppointmentModel }) => {
    const healthRouter = Router();
    const identifier = "/:id";
    /* Medicament */
    const medicamentEndpoint = '/medicament';
    const healthMedicamentController = new HealthMedicamentController({ medicamentModel });
    // GET. api/health/medicament
    healthRouter.get(`${medicamentEndpoint}`, [authMiddleware], healthMedicamentController.getAllMedicaments);
    // GET_BY_ID api/health/medicament/:id
    healthRouter.get(`${medicamentEndpoint}${identifier}`, [authMiddleware], healthMedicamentController.getMedicamentById);
    // POST
    healthRouter.post(`${medicamentEndpoint}`, [authMiddleware], healthMedicamentController.postNewMedicament);
    // PUT
    healthRouter.put(`${medicamentEndpoint}${identifier}`, [authMiddleware], healthMedicamentController.putUpdateMedicament);
    // PATCH
    healthRouter.patch(`${medicamentEndpoint}${identifier}`, [authMiddleware], healthMedicamentController.patchUpdateMedicament);
    // DELETE
    healthRouter.delete(`${medicamentEndpoint}${identifier}`, [authMiddleware], healthMedicamentController.deleteMedicament);
    /* MedicalAppointments */
    const medicalAppointmentEndpoint = '/medical_appointment';
    const healthMedicalAppointmentController = new HealthMedicalAppointmentController({ medicalAppointmentModel });
    // GET api/health/medical_appointment
    healthRouter.get(`${medicalAppointmentEndpoint}`, [authMiddleware], healthMedicalAppointmentController.getAllMedicalAppointments);
    // GET_APPOINTMENT_BY_ID api/health/medical_appointment/:id
    healthRouter.get(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], healthMedicalAppointmentController.getMedicalAppointmentById);
    // POST
    healthRouter.post(`${medicalAppointmentEndpoint}`, [authMiddleware], healthMedicalAppointmentController.postNewMedicalAppointment);
    // PUT
    healthRouter.put(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], healthMedicalAppointmentController.putUpdateMedicalAppointment);
    // PATCH
    healthRouter.patch(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], healthMedicalAppointmentController.patchUpdateMedicalAppointment);
    // DELETE
    healthRouter.delete(`${medicalAppointmentEndpoint}${identifier}`, [authMiddleware], healthMedicalAppointmentController.deleteMedicalAppointment);
    //
    return healthRouter;
}