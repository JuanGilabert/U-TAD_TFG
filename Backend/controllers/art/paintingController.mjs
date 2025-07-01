// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../config/GenericEnvConfig.mjs';
export class PaintingController {
    constructor({ model }) {
        this.model = model;
        this.getAllPaintings = this.getAllPaintings.bind(this);
        this.getPaintingById = this.getPaintingById.bind(this);
        this.getPaintingUnavailableDates = this.getPaintingUnavailableDates.bind(this);
        this.postPainting = this.postPainting.bind(this);
        this.putPainting = this.putPainting.bind(this);
        this.patchPainting = this.patchPainting.bind(this);
        this.deletePainting = this.deletePainting.bind(this);
    }
    //
    getAllPaintings = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaInicioExposicionArte = "hasNoValue", fechaFinExposicionArte = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllPaintingsModelResponse = await this.model.getAllPaintings(
                userId, fechaInicioExposicionArte, fechaFinExposicionArte
            );
            // Enviamos el error o la respuesta obtenida.
            return getAllPaintingsModelResponse === null ?
            res.status(404).json({ message: "Todavia no existe ninguna exposicion de arte a la que acudir." })
            : getAllPaintingsModelResponse === false ?
            res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllPaintingsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getPaintingById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getPaintingByIdModelResponse = await this.model.getPaintingById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getPaintingByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getPaintingByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getPaintingUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getPaintingUnavailableDatesModelResponse = await this.model.getPaintingUnavailableDates(userId);
            return !getPaintingUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getPaintingUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postPainting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const postNewPaintingModelResponse = await this.model.postPainting({ painting: req.body, userId });
            // Enviamos la respuesta obtenida.
            if (postNewPaintingModelResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita para la exposición.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putPainting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putPaintingModelResponse = await this.model.putPainting({ id, painting: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return putPaintingModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchPainting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchPaintingModelResponse = await this.model.patchPainting({ id, painting: req.body, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchPaintingModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchPaintingModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deletePainting = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deletePaintingModelResponse = await this.model.deletePainting(id, userId);
            // Enviamos el error obtenido.
            if (deletePaintingModelResponse) return res.status(204).send();
            // Enviamos la respuesta obtenida.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}