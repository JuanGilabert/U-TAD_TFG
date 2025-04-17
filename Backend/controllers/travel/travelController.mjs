// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewTravel, validatePartialNewTravel } from '../../models/travelModels/travelModelValidator.mjs';
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
        // Verificamos que no existan parametros en la peticion ni cuerpo en la misma.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllTravelsModelResponse = await this.model.getAllTravels(userId);
        // Enviamos el error.
        if (getAllTravelsModelResponse === false) return res.status(404).send({ message: "No existen viajes." });
        // Enviamos la respuesta.
        res.json(getAllTravelsModelResponse);
    }
    getTravelById = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getTravelByIdModelResponse = await this.model.getTravelById(id, userId);
        // Enviamos la respuesta.
        if (getTravelByIdModelResponse) return res.json(getTravelByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewTravel = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewTravel(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewTravelModelResponse = await this.model.postNewTravel({ ...result.data, userId });
        if (postNewTravelModelResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. No se ha podido crear el viaje.` });
    }
    putUpdateTravel = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewTravel(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateTravelModelResponse = await this.model.putUpdateTravel({ id, ...result.data, userId });
        if (putUpdateTravelModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateTravel = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewTravel(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateTravelModelResponse = await this.model.patchUpdateTravel({ id, ...result.data, userId });
        if (patchUpdateTravelModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(patchUpdateTravelModelResponse);
    }
    deleteTravel = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const deleteTravelModelResponse = await this.model.deleteTravel(id, userId);
        if (deleteTravelModelResponse) return res.status(204).json(deleteTravelModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: this.error404Message });
    }
}