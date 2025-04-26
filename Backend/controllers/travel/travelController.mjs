// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewTravel, validatePartialNewTravel } from '../../models/travel/travelModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class TravelController {
    constructor({ model }) { 
        this.model = model
        this.getAllTravels = this.getAllTravels.bind(this);
        this.getTravelById = this.getTravelById.bind(this);
        this.postNewTravel = this.postNewTravel.bind(this);
        this.putUpdateTravel = this.putUpdateTravel.bind(this);
        this.patchUpdateTravel = this.patchUpdateTravel.bind(this);
        this.deleteTravel = this.deleteTravel.bind(this);
    }
    //
    getAllTravels = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllTravelsModelResponse = await this.model.getAllTravels(userId);
        // Enviamos el error.
        if (getAllTravelsModelResponse === false) return res.status(404).send({ message: "No existen viajes." });
        // Enviamos la respuesta.
        res.status(200).json(getAllTravelsModelResponse);
    }
    getTravelById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getTravelByIdModelResponse = await this.model.getTravelById(id, userId);
        // Enviamos la respuesta.
        if (getTravelByIdModelResponse) return res.status(200).json(getTravelByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    postNewTravel = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewTravel(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewTravelModelResponse = await this.model.postNewTravel({ travel: result.data, userId });
        if (postNewTravelModelResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se ha podido crear el viaje.` });
    }
    putUpdateTravel = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewTravel(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateTravelModelResponse = await this.model.putUpdateTravel({ id, travel: result.data, userId });
        if (putUpdateTravelModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateTravel = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewTravel(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateTravelModelResponse = await this.model.patchUpdateTravel({ id, travel: result.data, userId });
        if (patchUpdateTravelModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta.
        res.status(200).json(patchUpdateTravelModelResponse);
    }
    deleteTravel = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const deleteTravelModelResponse = await this.model.deleteTravel(id, userId);
        if (deleteTravelModelResponse) return res.status(204).json(deleteTravelModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}