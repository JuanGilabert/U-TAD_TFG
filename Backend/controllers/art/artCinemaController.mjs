// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewCinema, validatePartialNewCinema } from '../../models/art/cinema/cinemaModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class CinemaController {
    constructor({ model }) {
        this.model = model;
        this.getAllCinemas = this.getAllCinemas.bind(this);
        this.getCinemaById = this.getCinemaById.bind(this);
        this.getCinemaUnavailableDates = this.getCinemaUnavailableDates.bind(this);
        this.postNewCinema = this.postNewCinema.bind(this);
        this.putUpdateCinema = this.putUpdateCinema.bind(this);
        this.patchUpdateCinema = this.patchUpdateCinema.bind(this);
        this.deleteCinema = this.deleteCinema.bind(this);
    }
    //
    getAllCinemas = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getAllCinemasModelResponse = await this.model.getAllCinemas(userId);
        // Enviamos el error.
        if (getAllCinemasModelResponse === false) return res.status(404).send({ message: "No existen citas." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getAllCinemasModelResponse);
    }
    getCinemaById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getCinemaByIdModelResponse = await this.model.getCinemaById(id, userId);
        if (getCinemaByIdModelResponse) return res.status(200).json(getCinemaByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getCinemaUnavailableDates = async (req, res) => {
        const { fechaInicioPelicula = "hasNoValue" } = req.query;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getCinemaUnavailableDatesModelResponse = await this.model.getCinemaUnavailableDates(userId, fechaInicioPelicula);
        // Enviamos los errores.
        if (getCinemaUnavailableDatesModelResponse?.message === "unavailableDatesError")
            return res.status(404).send({ message: "No existen fechas de reservas no disponibles." });
        if (getCinemaUnavailableDatesModelResponse?.message === "availableDatesError")
            return res.status(404).send({ message: "No existen citas para esta fecha." });
        // Enviamos la respuesta obtenida.
        return res.status(200).json(getCinemaUnavailableDatesModelResponse);
    }
    postNewCinema = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewCinema(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPostNewCinemaResponse = await this.model.postNewCinema({ cinema: result.data, userId });
        if (apiPostNewCinemaResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear la cita.` });
    }
    putUpdateCinema = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewCinema(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateCinemaResponse = await this.model.putUpdateCinema({ id, cinema: result.data, userId });
        // Enviamos el error.
        if (apiPutUpdateCinemaResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateCinema = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewCinema(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateCinemaResponse = await this.model.patchUpdateCinema({ id, cinema: result.data, userId });
        // Enviamos el error.
        if (apiPatchUpdateCinemaResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(apiPatchUpdateCinemaResponse);
    }
    deleteCinema = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta.
        const apiDeleteCinemaResponse = await this.model.deleteCinema(id, userId);
        if (apiDeleteCinemaResponse) return res.status(204).json(apiDeleteCinemaResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}