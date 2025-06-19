// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateWork, validatePartialWork } from '../../models/work/workModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class WorkController {
    constructor({ model }) { 
        this.model = model
        this.getAllWorks = this.getAllWorks.bind(this);
        this.getWorkById = this.getWorkById.bind(this);
        this.getWorkUnavailableDates = this.getWorkUnavailableDates.bind(this);
        this.postWork = this.postWork.bind(this);
        this.putWork = this.putWork.bind(this);
        this.patchWork = this.patchWork.bind(this);
        this.deleteWork = this.deleteWork.bind(this);
    }
    //
    getAllWorks = async (req, res) => {
        // Obtenemos los valores de la peticon.
        const { userId } = req.user;
        const { fechaInicioTarea = "hasNoValue", fechaEntregaTarea = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllWorksModelResponse = await this.model.getAllWorks(userId, fechaInicioTarea, fechaEntregaTarea);
            // Enviamos el error o la respuesta obtenida.
            return getAllWorksModelResponse === null ? res.status(404).json({ message: "Todavia no existe ninguna tarea." })
            : getAllWorksModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllWorksModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getWorkById = async (req, res) => {
        // Obtenemos los valores de la peticon.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getWorkByIdModelResponse = await this.model.getWorkById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getWorkByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getWorkByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getWorkUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticon.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getWorkUnavailableDatesModelResponse = await this.model.getWorkUnavailableDates(userId);
            // Enviamos el error o la respuesta obtenida.
            return !getWorkUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getWorkUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postWork = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const { userId } = req.user;
        try {
            const postWorkModelResponse = await this.model.postWork({ work: result.data, userId });
            // Enviamos la respuesta obtenida.
            if (postWorkModelResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear la tarea.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putWork = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos el id del recurso de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putWorkModelResponse = await this.model.putWork({ id, work: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putWorkModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchWork = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos el id del recurso de la peticion.
        const { id } = req.params;
        // Obtenemos del modelo los datos requeridos.
        try {
            const { userId } = req.user;
            const patchWorkModelResponse = await this.model.patchWork({ id, work: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchWorkModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchWorkModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteWork = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deleteWorkModelResponse = await this.model.deleteWork(id, userId);
            if (deleteWorkModelResponse) return res.status(204).send();
            // Enviamos el error.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}