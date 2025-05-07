// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMedicalAppointment, validatePartialNewMedicalAppointment } from '../../models/health/medicalAppointment/medicalAppointmentModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class HealthMedicalAppointmentController {
    constructor({ model }) {
        this.model = model;
        this.getAllMedicalAppointments = this.getAllMedicalAppointments.bind(this);
        this.getMedicalAppointmentById = this.getMedicalAppointmentById.bind(this);
        this.getMedicalAppointmentUnavailableDates = this.getMedicalAppointmentUnavailableDates.bind(this);
        this.postNewMedicalAppointment = this.postNewMedicalAppointment.bind(this);
        this.putUpdateMedicalAppointment = this.putUpdateMedicalAppointment.bind(this);
        this.patchUpdateMedicalAppointment = this.patchUpdateMedicalAppointment.bind(this);
        this.deleteMedicalAppointment = this.deleteMedicalAppointment.bind(this);
    }
    //
    getAllMedicalAppointments = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiGetAllMedicalAppointmentsResponse = await this.model.getAllMedicalAppointments(userId);
        // Enviamos el error.
        if (apiGetAllMedicalAppointmentsResponse === false) return res.status(404).send({ message: "No existe ninguna cita medica." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(apiGetAllMedicalAppointmentsResponse);
    }
    getMedicalAppointmentById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const apiGetMedicalAppointmentByIdResponse = await this.model.getMedicalAppointmentsById(id, userId);
        if (apiGetMedicalAppointmentByIdResponse) return res.status(200).json(apiGetMedicalAppointmentByIdResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getMedicalAppointmentUnavailableDates = async (req, res) => {
        const { fechaCitaMedica = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getMedicalAppointmentUnavailableDatesModelResponse = await this.model.getMedicalAppointmentUnavailableDates(userId, fechaCitaMedica);
        // Enviamos los errores.
        if (getMedicalAppointmentUnavailableDatesModelResponse?.message === "unavailableDatesError")
            return res.status(404).send({ message: "No existen fechas de reservas no disponibles." });
        if (getMedicalAppointmentUnavailableDatesModelResponse?.message === "availableDatesError")
            return res.status(404).send({ message: "No existen citas para esta fecha." });
        if (getMedicalAppointmentUnavailableDatesModelResponse?.message === "filteredAvailableDatesError")
            return res.status(404).send({ message: "No se pueden mostrar las reservas de esta fecha.\
            En esta fecha ya hay 3 citas o mas y no se pueden realizar reservas en esta fecha." });
        // Enviamos la respuesta obtenida.
        return res.status(200).json(getMedicalAppointmentUnavailableDatesModelResponse);
    }
    postNewMedicalAppointment = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validateNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos la cita medica del modelo de datos y respondemos a la peticion con la respuesta obtenida.
        const apiPostNewMedicalAppointmentResponse = await this.model.postNewMedicalAppointment({ medicalAppointment: result.data, userId });
        if (apiPostNewMedicalAppointmentResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita medica.` });
    }
    putUpdateMedicalAppointment = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validateNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateMedicalAppointmentResponse = await this.model.putUpdateMedicalAppointment({ id, medicalAppointment: result.data, userId });
        // Enviamos el error.
        if (apiPutUpdateMedicalAppointmentResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateMedicalAppointment = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = await validatePartialNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateMedicalAppointmentResponse = await this.model.patchUpdateMedicalAppointment({ id, medicalAppointment: result.data, userId });
        // Enviamos el error.
        if (apiPatchUpdateMedicalAppointmentResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).json(apiPatchUpdateMedicalAppointmentResponse);
    }
    deleteMedicalAppointment = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Eliminamos la cita medica del modelo de datos y enviamos la respuesta obtenida.
        const apiDeleteMedicalAppointmentResponse = await this.model.deleteMedicalAppointment(id, userId);
        if (apiDeleteMedicalAppointmentResponse) return res.status(204).json(apiDeleteMedicalAppointmentResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}