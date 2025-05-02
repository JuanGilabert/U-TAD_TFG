// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMusic, validatePartialNewMusic } from '../../models/art/music/musicModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class MusicController {
    constructor({ model }) {
        this.model = model;
        this.getAllMusics = this.getAllMusics.bind(this);
        this.getMusicById = this.getMusicById.bind(this);
        this.getMusicUnavailableDates = this.getMusicUnavailableDates.bind(this);
        this.postNewMusic = this.postNewMusic.bind(this);
        this.putUpdateMusic = this.putUpdateMusic.bind(this);
        this.patchUpdateMusic = this.patchUpdateMusic.bind(this);
        this.deleteMusic = this.deleteMusic.bind(this);
        //this.getVideoDownloadByUrl = this.getVideoDownloadByUrl.bind(this);
    }
    //
    getAllMusics = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllMusicsModelResponse = await this.model.getAllMusics(userId);
        // Enviamos el error.
        if (getAllMusicsModelResponse === false) return res.status(404).send({ message: "No existe ningun evento." });
        // Enviamos la respuesta obtenida
        res.status(200).json(getAllMusicsModelResponse);
    }
    getMusicById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos
        const getMusicByIdModelResponse = await this.model.getMusicById(id, userId);
        if (getMusicByIdModelResponse) return res.status(200).json(getMusicByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getMusicUnavailableDates = async (req, res) => {
        const { fechaInicioEvento = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getMusicUnavailableDatesModelResponse = await this.model.getMusicUnavailableDates(userId, fechaInicioEvento);
        // Enviamos los errores.
        if (getMusicUnavailableDatesModelResponse?.message === "unavailableDatesError")
            return res.status(404).send({ message: "No existen fechas de reservas no disponibles." });
        if (getMusicUnavailableDatesModelResponse?.message === "availableDatesError")
            return res.status(404).send({ message: "No existen citas para esta fecha." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getMusicUnavailableDatesModelResponse);
    }
    postNewMusic = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos
        const modelPostNewMusicResponse = await this.model.postNewMusic({ music: result.data, userId });
        if (modelPostNewMusicResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} Error al crear la cita para el evento.` });
    }
    putUpdateMusic = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelPutUpdateMusicResponse = await this.model.putUpdateMusic({ id, music: result.data, userId });
        // Enviamos el error.
        if (modelPutUpdateMusicResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateMusic = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewMusic(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelPatchUpdateMusicResponse = await this.model.patchUpdateMusic({ id, music: result.data, userId });
        // Enviamos el error.
        if (modelPatchUpdateMusicResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(modelPatchUpdateMusicResponse);
    }
    deleteMusic = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const modelDeleteMusicResponse = await this.model.deleteMusic(id, userId);
        if (modelDeleteMusicResponse) return res.status(204).json(modelDeleteMusicResponse);
        // Enviamos la respuesta obtenida.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    /*
    getVideoDownloadByUrl = async (req, res) => {
        const { id } = req.params;
        if (!id || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
        const modelGetVideoDownloadByUrlResponse = await this.model.getVideoDownloadByUrl(url);
        if (modelGetVideoDownloadByUrlResponse) return res.status(200).sendFile(modelGetVideoDownloadByUrlResponse);
        // Enviamos el error.        
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }*/
}