// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewWork, validatePartialNewWork } from '../../models/work/workModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class WorkController {
    constructor({ model }) { 
        this.model = model
        this.getAllWorks = this.getAllWorks.bind(this);
        this.getWorkById = this.getWorkById.bind(this);
        this.getWorkUnavailableDates = this.getWorkUnavailableDates.bind(this);
        this.postNewWork = this.postNewWork.bind(this);
        this.putUpdateWork = this.putUpdateWork.bind(this);
        this.patchUpdateWork = this.patchUpdateWork.bind(this);
        this.deleteWork = this.deleteWork.bind(this);
    }
    //
    getAllWorks = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllWorksModelResponse = await this.model.getAllWorks(userId);
        // Enviamos el error.
        if (getAllWorksModelResponse === false) return res.status(200).send({ message: "No existen tareas." });
        // Enviamos la respuesta.
        res.status(200).json(getAllWorksModelResponse);
    }
    getWorkById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getWorkByIdModelResponse = await this.model.getWorkById(id, userId);
        // Enviamos la respuesta.
        if (getWorkByIdModelResponse) return res.status(200).json(getWorkByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getWorkUnavailableDates = async (req, res) => {
        const { fechaInicioTarea = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getWorkUnavailableDatesModelResponse = await this.model.getWorkUnavailableDates(userId, fechaInicioTarea);
        // Enviamos los errores.
        if (getWorkUnavailableDatesModelResponse?.message === "unavailableDatesError") return res.status(200).send({ dates: [] });
        if (getWorkUnavailableDatesModelResponse?.message === "availableDatesError") return res.status(200).send({ dates: [] });
        if (getWorkUnavailableDatesModelResponse?.message === "filteredAvailableDatesError")
            return res.status(200).send({ message: "No se pueden mostrar las reservas de esta fecha.\
            En esta fecha ya hay 3 citas o mas y no se pueden realizar reservas en esta fecha." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getWorkUnavailableDatesModelResponse);
    }
    postNewWork = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewWorkModelResponse = await this.model.postNewWork({ work: result.data, userId });
        if (postNewWorkModelResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear la tarea.` });
    }
    putUpdateWork = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateWorkModelResponse = await this.model.putUpdateWork({ id, work: result.data, userId });
        if (putUpdateWorkModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateWork = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewWork(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateWorkModelResponse = await this.model.patchUpdateWork({ id, work: result.data, userId });
        if (patchUpdateWorkModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(patchUpdateWorkModelResponse);
    }
    deleteWork = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deleteWorkModelResponse = await this.model.deleteWork(id, userId);
        if (deleteWorkModelResponse) return res.status(204).json(deleteWorkModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}