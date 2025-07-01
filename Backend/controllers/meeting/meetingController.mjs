// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../config/GenericEnvConfig.mjs';
export class MeetingController {
    constructor({ model }) { 
        this.model = model
        this.getAllMeetings = this.getAllMeetings.bind(this);
        this.getMeetingById = this.getMeetingById.bind(this);
        this.getMeetingUnavailableDates = this.getMeetingUnavailableDates.bind(this);
        this.postMeeting = this.postMeeting.bind(this);
        this.putMeeting = this.putMeeting.bind(this);
        this.patchMeeting = this.patchMeeting.bind(this);
        this.deleteMeeting = this.deleteMeeting.bind(this);
    }
    //
    getAllMeetings = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaInicioReunion = "hasNoValue", fechaFinReunion = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllMeetingsModelResponse = await this.model.getAllMeetings(userId, fechaInicioReunion, fechaFinReunion);
            // Enviamos el error o la respuesta obtenida.
            return getAllMeetingsModelResponse === null ? res.status(404).json({ message: "Todavia no existe ninguna cita." })
            : getAllMeetingsModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllMeetingsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMeetingById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMeetingByIdModelResponse = await this.model.getMeetingById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getMeetingByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getMeetingByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMeetingUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMeetingUnavailableDatesModelResponse = await this.model.getMeetingUnavailableDates(userId);
            return !getMeetingUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getMeetingUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postMeeting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const postNewMeetingModelResponse = await this.model.postMeeting({ meeting: req.body, userId });
            // Enviamos la respuesta obtenida.
            if (postNewMeetingModelResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putMeeting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putMeetingModelResponse = await this.model.putMeeting({ id, meeting: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return putMeetingModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchMeeting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchMeetingModelResponse = await this.model.patchMeeting({ id, meeting: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchMeetingModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchMeetingModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteMeeting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deleteMeetingModelResponse = await this.model.deleteMeeting(id, userId);
            // Enviamos la respuesta obtenida.
            if (deleteMeetingModelResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}