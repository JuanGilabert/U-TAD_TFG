// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewSport, validatePartialNewSport } from '../../models/sport/sportModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class SportController {
    constructor({ model }) { 
        this.model = model
        this.getAllSports = this.getAllSports.bind(this);
        this.getSportById = this.getSportById.bind(this);
        this.postNewSport = this.postNewSport.bind(this);
        this.putUpdateSport = this.putUpdateSport.bind(this);
        this.patchUpdateSport = this.patchUpdateSport.bind(this);
        this.deleteSport = this.deleteSport.bind(this);
    }
    //
    getAllSports = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllSportsModelResponse = await this.model.getAllSports(userId);
        // Enviamos el error.
        if (getAllSportsModelResponse === false) return res.status(404).send({ message: "No hay actividades." });
        // Enviamos la respuesta.
        res.status(200).json(getAllSportsModelResponse);
    }
    getSportById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getSportByIdModelResponse = await this.model.getSportById(id, userId);
        // Enviamos la respuesta.
        if (getSportByIdModelResponse) return res.status(200).json(getSportByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    postNewSport = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewSportModelResponse = await this.model.postNewSport({ sport: result.data, userId });
        if (postNewSportModelResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear la actividad.` });
    }
    putUpdateSport = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateSportModelResponse = await this.model.putUpdateSport({ id, sport: result.data, userId });
        if (putUpdateSportModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateSport = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewSport(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateSportModelResponse = await this.model.patchUpdateSport({ id, sport: result.data, userId });
        if (patchUpdateSportModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(patchUpdateSportModelResponse);
    }
    deleteSport = async (req, res) => {
        const { id } = req.params
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const deleteSportModelResponse = await this.model.deleteSport(id, userId);
        if (deleteSportModelResponse) return res.status(204).json(deleteSportModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}