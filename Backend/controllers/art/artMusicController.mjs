// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMusic, validatePartialNewMusic } from '../../models/artModels/musicModel/musicModelValidator.mjs';
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
export class MusicController {
    /**
     * Constructor para el controlador de eventos musicales en Art Music.
     * @param {Object} model - Modelo de datos para la coleccion "Music".
     */
    constructor({ model }) {
        this.model = model;
        this.getAllMusics = this.getAllMusics.bind(this);
        this.getMusicById = this.getMusicById.bind(this);
        this.postNewMusic = this.postNewMusic.bind(this);
        this.putUpdateMusic = this.putUpdateMusic.bind(this);
        this.patchUpdateMusic = this.patchUpdateMusic.bind(this);
        this.deleteMusic = this.deleteMusic.bind(this);
        //this.getVideoDownloadByUrl = this.getVideoDownloadByUrl.bind(this);
    }
    //
    getAllMusics = async (req, res) => {
        // Verificamos que no existan parametros en la peticion ni tampoco cuerpo en la peticion.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllMusicsModelResponse = await this.model.getAllMusics(userId);
        // Enviamos el error.
        if (getAllMusicsModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(getAllMusicsModelResponse);
    }
    getMusicById = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos
        const getMusicByIdModelResponse = await this.model.getMusicById(id, userId);
        if (getMusicByIdModelResponse) return res.json(getMusicByIdModelResponse);
        //
        res.status(404).send({ message: error404Message });
    }
    postNewMusic = async (req, res) => {
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewCinema(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos
        const modelPostNewMusicResponse = await this.model.postNewMusic({ ...result.data, userId });
        if (modelPostNewMusicResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error
        res.status(500).send({ message: `${error500Message}. Error al crear la cita para el evento.` });
    }
    putUpdateMusic = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewMusic(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelPutUpdateMusicResponse = await this.model.putUpdateMusic({ id, ...result.data, userId });
        // Enviamos el error.
        if (modelPutUpdateMusicResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateMusic = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewMusic(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelPatchUpdateMusicResponse = await this.model.patchUpdateMusic({ id, ...result.data, userId });
        // Enviamos el error.
        if (modelPatchUpdateMusicResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(modelPatchUpdateMusicResponse);
    }
    deleteMusic = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelDeleteMusicResponse = await this.model.deleteMusic(id, userId);
        if (modelDeleteMusicResponse) return res.status(204).json(modelDeleteMusicResponse);
        // Enviamos la respuesta obtenida.
        res.status(404).send({ message: error404Message });
    }
    /*
    getVideoDownloadByUrl = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos. Verificamos que solo haya 1 parametro en la peticion.
        const { url } = req.params;
        if (!url || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        const modelGetVideoDownloadByUrlResponse = await this.model.getVideoDownloadByUrl(url);
        if (modelGetVideoDownloadByUrlResponse) return res.status(200).sendFile(modelGetVideoDownloadByUrlResponse);
        // Enviamos el error.        
        res.status(404).send({ message: error404Message });
    }*/
}