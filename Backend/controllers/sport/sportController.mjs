// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateSport, validatePartialSport } from '../../models/sport/sportModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class SportController {
    constructor({ model }) { 
        this.model = model
        this.getAllSports = this.getAllSports.bind(this);
        this.getSportById = this.getSportById.bind(this);
        this.getSportUnavailableDates = this.getSportUnavailableDates.bind(this);
        this.postSport = this.postSport.bind(this);
        this.putSport = this.putSport.bind(this);
        this.patchSport = this.patchSport.bind(this);
        this.deleteSport = this.deleteSport.bind(this);
    }
    //
    getAllSports = async (req, res) => {
        // Verificamos que los valores de la peticion sean validos.
        let result = "";
        if (fechaInicioActividad !== "hasNoValue") result = await validatePartialSport({ fechaInicioActividad });
        if (duracionActividadMinutos !== "hasNoValue") result = await validatePartialSport({ duracionActividadMinutos });
        if (result.error) return res.status(400).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const {
            fechaInicioActividad = "hasNoValue",
            duracionActividadMinutos = "hasNoValue"
        } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllSportsModelResponse = await this.model.getAllSports(userId, fechaInicioActividad, duracionActividadMinutos);
            return getAllSportsModelResponse === null ? res.status(404).json({ message: "Todavia no existe ninguna actividad." })
            : getAllSportsModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllSportsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getSportById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getSportByIdModelResponse = await this.model.getSportById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getSportByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getSportByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getSportUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getSportUnavailableDatesModelResponse = await this.model.getSportUnavailableDates(userId);
            // Enviamos el error o la respuesta obtenida.
            return !getSportUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getSportUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postSport = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const { userId } = req.user;
        try {
            const postSportModelResponse = await this.model.postSport({ sport: result.data, userId });
            if (postSportModelResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la actividad.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putSport = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putSportModelResponse = await this.model.putSport({ id, sport: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putSportModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchSport = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchSportModelResponse = await this.model.patchSport({ id, sport: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchSportModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchSportModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteSport = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deleteSportModelResponse = await this.model.deleteSport(id, userId);
            // Enviamos la respuesta obtenida.
            if (deleteSportModelResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}