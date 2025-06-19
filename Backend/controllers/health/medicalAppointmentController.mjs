// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateMedicalAppointment, validatePartialMedicalAppointment } from '../../models/health/medicalAppointment/medicalAppointmentModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class HealthMedicalAppointmentController {
    constructor({ model }) {
        this.model = model;
        this.getAllMedicalAppointments = this.getAllMedicalAppointments.bind(this);
        this.getMedicalAppointmentById = this.getMedicalAppointmentById.bind(this);
        this.getMedicalAppointmentUnavailableDates = this.getMedicalAppointmentUnavailableDates.bind(this);
        this.postMedicalAppointment = this.postMedicalAppointment.bind(this);
        this.putMedicalAppointment = this.putMedicalAppointment.bind(this);
        this.patchMedicalAppointment = this.patchMedicalAppointment.bind(this);
        this.deleteMedicalAppointment = this.deleteMedicalAppointment.bind(this);
    }
    //
    getAllMedicalAppointments = async (req, res) => {
        // Verificamos que los valores de la peticion sean validos.
        let result = "";
        if (fechaCitaMedica !== "hasNoValue") result = await validatePartialMedicalAppointment({ fechaCitaMedica });
        if (tipoPruebaCitaMedica !== "hasNoValue") result = await validatePartialMedicalAppointment({ tipoPruebaCitaMedica });
        if (result.error) return res.status(400).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaCitaMedica = "hasNoValue", tipoPruebaCitaMedica = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const apiGetAllMedicalAppointmentsResponse = await this.model.getAllMedicalAppointments(
                userId, fechaCitaMedica, tipoPruebaCitaMedica
            );
            // Enviamos el error o la respuesta obtenida.
            return apiGetAllMedicalAppointmentsResponse === null ?
            res.status(404).json({ message: "Todavia no existen citas medicas." })
            : apiGetAllMedicalAppointmentsResponse === false ?
            res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(apiGetAllMedicalAppointmentsResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMedicalAppointmentById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const apiGetMedicalAppointmentByIdResponse = await this.model.getMedicalAppointmentsById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !this.getAllMedicalAppointments ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(apiGetMedicalAppointmentByIdResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMedicalAppointmentUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMedicalAppointmentUnavailableDatesModelResponse = await this.model.getMedicalAppointmentUnavailableDates(userId);
            return !getMedicalAppointmentUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getMedicalAppointmentUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postMedicalAppointment = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validateMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo de datos los datos requeridos.
        const { userId } = req.user;
        try {
            const apiPostNewMedicalAppointmentResponse = await this.model.postMedicalAppointment({ medicalAppointment: result.data, userId });
            // Enviamos la respuesta obtenida.
            if (apiPostNewMedicalAppointmentResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita medica.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putMedicalAppointment = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validateMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putMedicalAppointmentModelResponse = await this.model.putMedicalAppointment({ id, medicalAppointment: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putMedicalAppointmentModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchMedicalAppointment = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validatePartialMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchMedicalAppointmentModelResponse = await this.model.patchMedicalAppointment({ id, medicalAppointment: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchMedicalAppointmentModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchMedicalAppointmentModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteMedicalAppointment = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Eliminamos los datos requeridos.
        try {
            const apiDeleteMedicalAppointmentResponse = await this.model.deleteMedicalAppointment(id, userId);
            // Enviamos la respuesta obtenida.
            if (apiDeleteMedicalAppointmentResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}