// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewSport, validatePartialNewSport } from '../../models/sportModels/sportModelValidator.mjs';
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
        // Verificamos que no existan parametros en la peticion ni cuerpo en la misma.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllSportsModelResponse = await this.model.getAllSports(userId);
        // Enviamos el error.
        if (getAllSportsModelResponse === false) return res.status(404).send({ message: "No hay actividades." });
        // Enviamos la respuesta.
        res.json(getAllSportsModelResponse);
    }
    getSportById = async (req, res) => {
        // Verificamos que no haya queries ni tampoco body.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getSportByIdModelResponse = await this.model.getAllSports(id, userId);
        // Enviamos la respuesta.
        if (getSportByIdModelResponse) return res.json(getSportByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewSport = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewSport(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewSportModelResponse = await this.model.postNewSport({ ...result.data, userId });
        if (postNewSportModelResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. No se ha podido crear la actividad.` });
    }
    putUpdateSport = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewSport(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateSportModelResponse = await this.model.putUpdateSport({ id, ...result.data, userId });
        if (putUpdateSportModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateSport = async (req, res) => {
        //
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewSport(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const patchUpdateSportModelResponse = await this.model.patchUpdateSport({ id, ...result.data, userId });
        if (patchUpdateSportModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(patchUpdateSportModelResponse);
    }
    deleteSport = async (req, res) => {
        // Verificamos que no haya queries ni tampoco body.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const deleteSportModelResponse = await this.model.deleteSport(id, userId);
        if (deleteSportModelResponse) return res.status(204).json(deleteSportModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
}