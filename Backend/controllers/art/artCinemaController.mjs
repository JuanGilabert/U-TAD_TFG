// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewCinema, validatePartialNewCinema } from '../../models/artModels/cinemaModel/cinemaModelValidator.mjs';
import { randomUUIDv4Regex } from '../../utils/export/GenericRegex.mjs';
import { findUserIdByEmailFunction } from '../../utils/functions/findUserIdByEmailFunction.mjs';
// Creamos mensajes genericos.
const okey200Message = process.env.OKEY_200_MESSAGE;
const okey201Message = process.env.CREATED_201_MESSAGE;
const error400Message = process.env.BAD_REQUEST_400_MESSAGE;
const error404Message = process.env.NOT_FOUND_404_MESSAGE;
const error406Message = process.env.NOT_ACCEPTABLE_406_MESSAGE;
const error500Message = process.env.INTERNAL_SERVER_ERROR_500_MESSAGE;
const errorQueryMessage = "Esta ruta no acepta query parameters";
////
export class CinemaController {
    constructor({ model }) {
        this.model = model;
        this.getAllCinemas = this.getAllCinemas.bind(this);
        this.getCinemaById = this.getCinemaById.bind(this);
        this.postNewCinema = this.postNewCinema.bind(this);
        this.putUpdateCinema = this.putUpdateCinema.bind(this);
        this.patchUpdateCinema = this.patchUpdateCinema.bind(this);
        this.deleteCinema = this.deleteCinema.bind(this);
    }
    //
    getAllCinemas = async (req, res) => {
        if (!req.accepts('json')) return res.status(406).json({ error: error406Message });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.body).length > 0)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getAllCinemasModelResponse = await this.model.getAllCinemas(userId);
        // Enviamos el error.
        if (getAllCinemasModelResponse === false) return res.status(404).send({ message: "No existen citas medicas." });
        // Enviamos la respuesta obtenida
        res.json(getAllCinemasModelResponse);
    }
    getCinemaById = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no exista cuerpo en la peticion.
        if (Object.keys(req.body).length > 0)
            return res.status(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        // Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getCinemaByIdModelResponse = await this.model.getCinemaById(id, userId);
        if (getCinemaByIdModelResponse) return res.json(getCinemaByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewCinema = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion
        if (Object.keys(req.params).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewCinema(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPostNewCinemaResponse = await this.model.postNewCinema({ ...result.data, userId });
        if (apiPostNewCinemaResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. No se ha podido crear la cita.` });
    }
    putUpdateCinema = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        // Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewCinema(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateCinemaResponse = await this.model.putUpdateCinema({ id, ...result.data, userId });
        // Enviamos el error.
        if (apiPutUpdateCinemaResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateCinema = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        // Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewCinema(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateCinemaResponse = await this.model.patchUpdateCinema({ id, ...result.data, userId });
        // Enviamos el error.
        if (apiPatchUpdateCinemaResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(apiPatchUpdateCinemaResponse);
    }
    deleteCinema = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no exista cuerpo en la peticion.
        if (Object.keys(req.body).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        // Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta.
        const apiDeleteCinemaResponse = await this.model.deleteCinema(id, userId);
        if (apiDeleteCinemaResponse) return res.status(204).json(apiDeleteCinemaResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
}