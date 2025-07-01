// Importamos los mensajes de respuesta para responder con los mismos a las peticiones.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../config/GenericEnvConfig.mjs';
////
export class TravelController {
    constructor({ model }) { 
        this.model = model
        this.getAllTravels = this.getAllTravels.bind(this);
        this.getTravelById = this.getTravelById.bind(this);
        this.getTravelDates = this.getTravelDates.bind(this);
        this.postTravel = this.postTravel.bind(this);
        this.putTravel = this.putTravel.bind(this);
        this.patchTravel = this.patchTravel.bind(this);
        this.deleteTravel = this.deleteTravel.bind(this);
    }
    //
    getAllTravels = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const {
            fechaSalidaViaje = "hasNoValue", fechaRegresoViaje = "hasNoValue"
        } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllTravelsModelResponse = await this.model.getAllTravels(userId, fechaSalidaViaje, fechaRegresoViaje);
            // Enviamos el error o la respuesta obtenida.
            return getAllTravelsModelResponse === null ? res.status(404).json({ message: "Todavia no existe ningun viaje." })
            : getAllTravelsModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllTravelsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    //
    getTravelById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getTravelByIdModelResponse = await this.model.getTravelById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getTravelByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getTravelByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getTravelDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getTravelUnavailableDatesModelResponse = await this.model.getTravelDates(userId);
            // Enviamos el error o la respuesta obtenida.
            return !getTravelUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getTravelUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postTravel = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const postTravelModelResponse = await this.model.postTravel({ travel: req.body, userId });
            // Enviamos la respuesta obtenida.
            if (postTravelModelResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear el viaje.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putTravel = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putTravelModelResponse = await this.model.putTravel({ id, travel: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return putTravelModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchTravel = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchTravelModelResponse = await this.model.patchTravel({ id, travel: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchTravelModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchTravelModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteTravel = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deleteTravelModelResponse = await this.model.deleteTravel(id, userId);
            // Enviamos la respuesta obtenida.
            if (deleteTravelModelResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}