// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewPainting, validatePartialNewPainting } from '../../models/artModels/paintingModel/paintingModelValidator.mjs';
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
export class PaintingController {
    constructor({ model }) {
        this.model = model;
        this.getAllPaintings = this.getAllPaintings.bind(this);
        this.getPaintingById = this.getPaintingById.bind(this);
        this.postNewPainting = this.postNewPainting.bind(this);
        this.putUpdatePainting = this.putUpdatePainting.bind(this);
        this.patchUpdatePainting = this.patchUpdatePainting.bind(this);
        this.deletePainting = this.deletePainting.bind(this);
    }
    //
    getAllPaintings = async (req, res) => {
        // Verificamos que no existan parametros en la peticion ni tampoco cuerpo en la peticion.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllPaintingsModelResponse = await this.model.getAllPaintings(userId);
        // Enviamos el error.
        if (getAllPaintingsModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(getAllPaintingsModelResponse);
    }
    getPaintingById = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const getPaintingByIdModelResponse = await this.model.getPaintingById(id, userId);
        if (getPaintingByIdModelResponse) return res.json(getPaintingByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewPainting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.status(400).send({ message: error400Message });
        // Validamos que el cuerpo de la peticion sea valido.
        const result = validateNewPainting(req.body);
        if (result.error) return res.status(422).send({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewPaintingModelResponse = await this.model.postNewPainting({ ...result.data, userId });
        if (postNewPaintingModelResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. Error al crear la cita para el evento.` });
    }
    putUpdatePainting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewPainting(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdatePaintingModelResponse = await this.model.putUpdatePainting({ id, ...result.data, userId });
        // Enviamos el error.
        if (putUpdatePaintingModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdatePainting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewPainting(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const patchUpdatePaintingModelResponse = await this.model.patchUpdatePainting({ id, ...result.data, userId });
        // Enviamos el error.
        if (patchUpdatePaintingModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(patchUpdatePaintingModelResponse);
    }
    deletePainting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que solo haya 1 parametro en la peticion.
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deletePaintingModelResponse = await this.model.deletePainting(id, userId);
        if (deletePaintingModelResponse) return res.status(204).json(deletePaintingModelResponse);
        // Enviamos la respuesta obtenida.
        res.status(404).send({ message: error404Message });
    }
}