// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMeeting, validatePartialNewMeeting } from '../../models/meetingModels/meetingModelValidator.mjs';
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
export class MeetingController {
    constructor({ model }) { 
        this.model = model
        this.getAllMeetings = this.getAllMeetings.bind(this);
        this.getMeetingById = this.getMeetingById.bind(this);
        this.postNewMeeting = this.postNewMeeting.bind(this);
        this.putUpdateMeeting = this.putUpdateMeeting.bind(this);
        this.patchUpdateMeeting = this.patchUpdateMeeting.bind(this);
        this.deleteMeeting = this.deleteMeeting.bind(this);
    }
    //
    getAllMeetings = async (req, res) => {
        // Verificamos que no existan parametros en la peticion ni cuerpo en la misma.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllMeetingsModelResponse = await this.model.getAllMedicaments(userId);
        // Enviamos el error.
        if (getAllMeetingsModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(getAllMeetingsModelResponse);
    }
    getMeetingById = async (req, res) => {
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
        const getMeetingByIdModelResponse = await this.model.getMedicamentById(id, userId);
        // Enviamos la respuesta.
        if (getMeetingByIdModelResponse) return res.json(getMeetingByIdModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewMeeting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewMeeting(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const postNewMeetingModelResponse = await this.model.postNewMeeting({ ...result.data, userId });
        if (postNewMeetingModelResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el error.
        res.status(500).send({ message: `${error500Message}. No se ha podido crear la cita.` });
    }
    putUpdateMeeting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewMeeting(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdateMeetingModelResponse = await this.model.putUpdateMeeting({ id, ...result.data, userId });
        // Enviamos el error.
        if (putUpdateMeetingModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateMeeting = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewMeeting(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Si no hay body valido devolvemos un error.
        const putUpdateMeetingModelResponse = await this.model.putUpdateMeeting({ id, ...result.data, userId });
        if (putUpdateMeetingModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta.
        res.json(putUpdateMeetingModelResponse);
    }
    deleteMeeting = async (req, res) => {
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
        const deleteMeetingModelResponse = await this.model.deleteMeeting(id, userId);
        if (deleteMeetingModelResponse) return res.status(204).json(deleteMeetingModelResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
}