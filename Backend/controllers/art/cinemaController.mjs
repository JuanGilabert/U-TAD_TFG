// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class CinemaController {
    constructor({ model }) {
        this.model = model;
        this.getAllCinemas = this.getAllCinemas.bind(this);
        this.getCinemaById = this.getCinemaById.bind(this);
        this.getCinemaUnavailableDates = this.getCinemaUnavailableDates.bind(this);
        this.postCinema = this.postCinema.bind(this);
        this.putCinema = this.putCinema.bind(this);
        this.patchCinema = this.patchCinema.bind(this);
        this.deleteCinema = this.deleteCinema.bind(this);
    }
    //
    getAllCinemas = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaInicioPelicula = "hasNoValue", duracionPeliculaMinutos = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllCinemasModelResponse = await this.model.getAllCinemas(userId, fechaInicioPelicula, duracionPeliculaMinutos);
            // Enviamos el error o la respuesta obtenida.
            return getAllCinemasModelResponse === null ?
            res.status(404).json({ message: "Todavia no existe ninguna cita." })
            : getAllCinemasModelResponse === false ?//posible 204
            res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllCinemasModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getCinemaById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getCinemaByIdModelResponse = await this.model.getCinemaById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getCinemaByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getCinemaByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getCinemaUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getCinemaUnavailableDatesModelResponse = await this.model.getCinemaUnavailableDates(userId);
            // Enviamos el error o la respuesta obtenida.
            return getCinemaUnavailableDatesModelResponse === null ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : getCinemaUnavailableDatesModelResponse === false ? res.status(204).send()
            : res.status(200).json(getCinemaUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postCinema = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const apiPostNewCinemaResponse = await this.model.postCinema({ cinema: req.body, userId });
            // Enviamos la respuesta obtenida.
            if (apiPostNewCinemaResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita para el evento.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putCinema = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putCinemaModelResponse = await this.model.putCinema({ id, cinema: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return putCinemaModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchCinema = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchCinemaModelResponse = await this.model.patchCinema({ id, cinema: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchCinemaModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchCinemaModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteCinema = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const apiDeleteCinemaResponse = await this.model.deleteCinema(id, userId);
            // Enviamos la respuesta obtenida.
            if (apiDeleteCinemaResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}