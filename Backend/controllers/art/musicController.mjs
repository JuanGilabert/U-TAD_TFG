// Importamos los modelos/schemas para validar los datos de las peticiones
import { get } from 'node:http';
import { validateMusic, validatePartialMusic } from '../../models/art/music/musicModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    BAD_REQUEST_400_MESSAGE, NOT_FOUND_404_MESSAGE, UNPROCESSABLE_ENTITY_422_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class MusicController {
    constructor({ model }) {
        this.model = model;
        this.getAllMusics = this.getAllMusics.bind(this);
        this.getMusicById = this.getMusicById.bind(this);
        this.getMusicUnavailableDates = this.getMusicUnavailableDates.bind(this);
        this.postMusic = this.postMusic.bind(this);
        this.putMusic = this.putMusic.bind(this);
        this.patchMusic = this.patchMusic.bind(this);
        this.deleteMusic = this.deleteMusic.bind(this);
        this.postMusicVideoDownload = this.postMusicVideoDownload.bind(this);
    }
    //
    getAllMusics = async (req, res) => {//CON-OK MOD-OK
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaInicioEvento  = "hasNoValue", fechaFinEvento = "hasNoValue" } = req.query;
        // Verificamos que los valores de la peticion sean validos.
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllMusicsModelResponse = await this.model.getAllMusics(userId, fechaInicioEvento, fechaFinEvento);
            return getAllMusicsModelResponse === null ? res.status(404).json({ message: "Todavia no existe ningun evento." })
            : getAllMusicsModelResponse === false ? res.status(400).json({ message: BAD_REQUEST_400_MESSAGE })//REVISAR ESTE 400 POR 404
            : res.status(200).json(getAllMusicsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMusicById = async (req, res) => {//CON-OK MOD-OK
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMusicByIdModelResponse = await this.model.getMusicById(id, userId);
            // Enviamos el error  o la respuesta obtenida.
            return !getMusicByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getMusicByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMusicUnavailableDates = async (req, res) => {//CON-OK MOD-OK
        // Hay que distinguir cuando las fechas no existen debido a que
        // el userId estan mal porque buscas fechas en la coleccion que no son tuyas(404)
        // o cuando realmente son tuyas y no tienes la suficiente cantidad(200)
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMusicUnavailableDatesModelResponse = await this.model.getMusicUnavailableDates(userId);
            // Enviamos el error o la respuesta obtenida. Puede ser una lista vacia o una lista de string con las fechas.
            return !getMusicUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getMusicUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postMusic = async (req, res) => { //CON-OK MOD-OK
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const { userId } = req.user;
        try {
            const modelPostNewMusicResponse = await this.model.postMusic({ music: result.data, userId });
            // Enviamos la respuesta obtenida.
            if (modelPostNewMusicResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido. La operación de inserción no fue reconocida por MongoDB.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} Error al crear la cita para el evento.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putMusic = async (req, res) => {//CON-OK MOD-OK
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putMusicModelResponse = await this.model.putMusic({ id, music: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putMusicModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchMusic = async (req, res) => {//CON-OK MOD-OK
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchMusicModelResponse = await this.model.patchMusic({ id, music: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchMusicModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchMusicModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteMusic = async (req, res) => {//CON-REV MOD-REV
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const modelDeleteMusicResponse = await this.model.deleteMusic(id, userId);
            // Enviamos el error obtenido.
            if (modelDeleteMusicResponse) return res.status(204).send();
            // Enviamos la respuesta obtenida.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postMusicVideoDownload = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateMusicVideoDownload(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMusicVideoDownloadByUrlModelResponse = await this.model.postMusicVideoDownload({ videoData: result.data });
            // Enviamos el error.
            if (getMusicVideoDownloadByUrlModelResponse?.type === "Warning")
                return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
            if (getMusicVideoDownloadByUrlModelResponse?.type === "UnknownWarning")
                return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
            if (getMusicVideoDownloadByUrlModelResponse?.type === "Error")
                return res.status(400).json({
                    message: `${BAD_REQUEST_400_MESSAGE} ${getMusicVideoDownloadByUrlModelResponse?.message}`
                });
            // Enviamos la respuesta obtenida.
            return res.status(201).download(getMusicVideoDownloadByUrlModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
}