// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMeeting, validatePartialNewMeeting } from '../../models/meeting/meetingModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class MeetingController {
    constructor({ model }) { 
        this.model = model
        this.getAllMeetings = this.getAllMeetings.bind(this);
        this.getMeetingById = this.getMeetingById.bind(this);
        this.getMeetingUnavailableDates = this.getMeetingUnavailableDates.bind(this);
        this.postNewMeeting = this.postNewMeeting.bind(this);
        this.putUpdateMeeting = this.putUpdateMeeting.bind(this);
        this.patchUpdateMeeting = this.patchUpdateMeeting.bind(this);
        this.deleteMeeting = this.deleteMeeting.bind(this);
    }
    //
    getAllMeetings = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllMeetingsModelResponse = await this.model.getAllMeetings(userId);
        // Enviamos el error.
        if (getAllMeetingsModelResponse === false) return res.status(404).send({ message: "No existen citas." });
        // Enviamos la respuesta.
        res.status(200).json(getAllMeetingsModelResponse);
    }
    getMeetingById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getMeetingByIdModelResponse = await this.model.getMeetingById(id, userId);
        // Enviamos la respuesta.
        if (getMeetingByIdModelResponse) return res.status(200).json(getMeetingByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getMeetingUnavailableDates = async (req, res) => {
        const { fechaInicioReunion = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getMeetingUnavailableDatesModelResponse = await this.model.getMeetingUnavailableDates(userId, fechaInicioReunion);
        // Enviamos los errores.
        if (getMeetingUnavailableDatesModelResponse?.message === "unavailableDatesError")
            return res.status(404).send({ message: "No existen fechas de reservas no disponibles." });
        if (getMeetingUnavailableDatesModelResponse?.message === "availableDatesError")
            return res.status(404).send({ message: "No existen citas para esta fecha." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getMeetingUnavailableDatesModelResponse);
    }
    postNewMeeting = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewMeeting(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewMeetingModelResponse = await this.model.postNewMeeting({ meeting: result.data, userId });
        if (postNewMeetingModelResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear la cita.` });
    }
    putUpdateMeeting = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewMeeting(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdateMeetingModelResponse = await this.model.putUpdateMeeting({ id, meeting: result.data, userId });
        // Enviamos el error.
        if (putUpdateMeetingModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateMeeting = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewMeeting(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateMeetingModelResponse = await this.model.patchUpdateMeeting({ id, meeting: result.data, userId });
        if (putUpdateMeetingModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(putUpdateMeetingModelResponse);
    }
    deleteMeeting = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deleteMeetingModelResponse = await this.model.deleteMeeting(id, userId);
        if (deleteMeetingModelResponse) return res.status(204).json(deleteMeetingModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}