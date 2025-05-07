// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewPainting, validatePartialNewPainting } from '../../models/art/painting/paintingModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class PaintingController {
    constructor({ model }) {
        this.model = model;
        this.getAllPaintings = this.getAllPaintings.bind(this);
        this.getPaintingById = this.getPaintingById.bind(this);
        this.getPaintingUnavailableDates = this.getPaintingUnavailableDates.bind(this);
        this.postNewPainting = this.postNewPainting.bind(this);
        this.putUpdatePainting = this.putUpdatePainting.bind(this);
        this.patchUpdatePainting = this.patchUpdatePainting.bind(this);
        this.deletePainting = this.deletePainting.bind(this);
    }
    //
    getAllPaintings = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllPaintingsModelResponse = await this.model.getAllPaintings(userId);
        // Enviamos el error.
        if (getAllPaintingsModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(getAllPaintingsModelResponse);
    }
    getPaintingById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const getPaintingByIdModelResponse = await this.model.getPaintingById(id, userId);
        if (getPaintingByIdModelResponse) return res.status(200).json(getPaintingByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getPaintingUnavailableDates = async (req, res) => {
        const { fechaInicioExposicionArte = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getPaintingUnavailableDatesModelResponse = await this.model.getPaintingUnavailableDates(userId, fechaInicioExposicionArte);
        // Enviamos los errores.
        if (getPaintingUnavailableDatesModelResponse?.message === "unavailableDatesError")
            return res.status(404).send({ message: "No existen fechas de reservas no disponibles." });
        if (getPaintingUnavailableDatesModelResponse?.message === "availableDatesError")
            return res.status(404).send({ message: "No existen citas para esta fecha." });
        if (getPaintingUnavailableDatesModelResponse?.message === "filteredAvailableDatesError")
            return res.status(404).send({ message: "No se pueden mostrar las reservas de esta fecha.\
            En esta fecha ya hay 3 citas o mas y no se pueden realizar reservas en esta fecha." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getPaintingUnavailableDatesModelResponse);
    }
    postNewPainting = async (req, res) => {
        // Validamos que el cuerpo de la peticion sea valido.
        const result = await validateNewPainting(req.body);
        if (result.error) return res.status(422).send({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewPaintingModelResponse = await this.model.postNewPainting({ painting: result.data, userId });
        if (postNewPaintingModelResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear la cita para el evento.` });
    }
    putUpdatePainting = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewPainting(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdatePaintingModelResponse = await this.model.putUpdatePainting({ id, painting: result.data, userId });
        // Enviamos el error.
        if (putUpdatePaintingModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdatePainting = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewPainting(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const patchUpdatePaintingModelResponse = await this.model.patchUpdatePainting({ id, painting: result.data, userId });
        // Enviamos el error.
        if (patchUpdatePaintingModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(patchUpdatePaintingModelResponse);
    }
    deletePainting = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deletePaintingModelResponse = await this.model.deletePainting(id, userId);
        if (deletePaintingModelResponse) return res.status(204).json(deletePaintingModelResponse);
        // Enviamos la respuesta obtenida.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}