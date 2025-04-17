// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewWork, validatePartialNewWork } from '../../models/workModels/workModelValidator.mjs';
import { randomUUIDv4Regex } from '../../utils/export/GenericRegex.mjs';
import { findUserIdByEmailFunction } from '../../utils/functions/findUserIdByEmailFunction.mjs';
// Creamos mensajes genericos.
const okey200Message = process.env.OKEY_200_MESSAGE;
const okey201Message = process.env.CREATED_201_MESSAGE;
const error400Message = process.env.BAD_REQUEST_400_MESSAGE;
const error404Message = process.env.NOT_FOUND_404_MESSAGE;
const error500Message = process.env.INTERNAL_SERVER_ERROR_500_MESSAGE;
const errorQueryMessage = "Esta ruta no acepta query parameters";
////
export class WorkController {
    constructor({ model }) { 
        this.model = model
        this.getAllWorks = this.getAllWorks.bind(this);
        this.getWorkById = this.getWorkById.bind(this);
        this.postNewWork = this.postNewWork.bind(this);
        this.putUpdateWork = this.putUpdateWork.bind(this);
        this.patchUpdateWork = this.patchUpdateWork.bind(this);
        this.deleteWork = this.deleteWork.bind(this);
    }
    //
    getAllWorks = async (req, res) => {
        // Verificamos que no existan parametros en la peticion ni cuerpo en la misma.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllWorksModelResponse = await this.model.getAllWorks(userId);
        // Enviamos el error.
        if (getAllWorksModelResponse === false) return res.status(404).send({ message: "No existen tareas." });
        // Enviamos la respuesta.
        res.json(getAllWorksModelResponse);
    }
    getWorkById = async (req, res) => {
        // Verificamos que no existan queries en la peticion ni tampoco cuerpo en la misma.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getWorkByIdModelResponse = await this.model.getWorkById(id, userId);
        // Enviamos la respuesta.
        if (getWorkByIdModelResponse) return res.json(getWorkByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewWork = async (req, res) => {
        // Verificamos que no existan queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewWork(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewWorkModelResponse = await this.model.postNewWork({ ...result.data, userId });
        if (postNewWorkModelResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. No se ha podido crear la tarea.` });
    }
    putUpdateWork = async (req, res) => {
        // Verificamos que no existan queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewWork(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateWorkModelResponse = await this.model.putUpdateWork({ id, ...result.data, userId });
        if (putUpdateWorkModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateWork = async (req, res) => {
        // Verificamos que no existan queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewWork(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateWorkModelResponse = await this.model.patchUpdateWork({ id, ...result.data, userId });
        if (patchUpdateWorkModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(patchUpdateWorkModelResponse);
    }
    deleteWork = async (req, res) => {
        // Verificamos que no existan queries en la peticion ni tampoco cuerpo en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deleteWorkModelResponse = await this.model.deleteWork(id, userId);
        if (deleteWorkModelResponse) return res.status(204).json(deleteWorkModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
}