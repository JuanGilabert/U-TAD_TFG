// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewMedicalAppointment, validatePartialNewMedicalAppointment } from '../../models/healthModels/model/modelValidator.mjs';
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
export class HealthMedicalAppointmentController {
    constructor({ model }) {
        this.model = model;
        this.getAllMedicalAppointments = this.getAllMedicalAppointments.bind(this);
        this.getMedicalAppointmentById = this.getMedicalAppointmentById.bind(this);
        this.postNewMedicalAppointment = this.postNewMedicalAppointment.bind(this);
        this.putUpdateMedicalAppointment = this.putUpdateMedicalAppointment.bind(this);
        this.patchUpdateMedicalAppointment = this.patchUpdateMedicalAppointment.bind(this);
        this.deleteMedicalAppointment = this.deleteMedicalAppointment.bind(this);
    }
    //
    getAllMedicalAppointments = async (req, res) => {
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiGetAllMedicalAppointmentsResponse = await this.model.getAllMedicalAppointments(userId);
        // Enviamos el error.
        if (apiGetAllMedicalAppointmentsResponse === false) return res.status(404).send({ message: "No existen citas medicas." });
        // Enviamos la respuesta obtenida.
        res.json(apiGetAllMedicalAppointmentsResponse);
    }
    getMedicalAppointmentById = async (req, res) => {
        // Validamos que no haya cuerpo ni queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const apiGetMedicalAppointmentByIdResponse = await this.model.getMedicalAppointmentsById(id, userId);
        if (apiGetMedicalAppointmentByIdResponse) return res.json(apiGetMedicalAppointmentByIdResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
    postNewMedicalAppointment = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = validateNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos la cita medica del modelo de datos y respondemos a la peticion con la respuesta obtenida.
        const apiPostNewMedicalAppointmentResponse = await this.model.postNewMedicalAppointment({ ...result.data, userId });
        if (apiPostNewMedicalAppointmentResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: `${error500Message}. Error al crear la cita medica.` });
    }
    putUpdateMedicalAppointment = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = validateNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateMedicalAppointmentResponse = await this.model.putUpdateMedicalAppointment({ id, ...result.data, userId });
        // Enviamos el error.
        if (apiPutUpdateMedicalAppointmentResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateMedicalAppointment = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error
        const result = validatePartialNewMedicalAppointment(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateMedicalAppointmentResponse = await this.model.patchUpdateMedicalAppointment({ id, ...result.data, userId });
        // Enviamos el error.
        if (apiPatchUpdateMedicalAppointmentResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.json(apiPatchUpdateMedicalAppointmentResponse);
    }
    deleteMedicalAppointment = async (req, res) => {
        // Validamos que no haya cuerpo ni queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Eliminamos la cita medica del modelo de datos y enviamos la respuesta obtenida.
        const apiDeleteMedicalAppointmentResponse = await this.model.deleteMedicalAppointment(id, userId);
        if (apiDeleteMedicalAppointmentResponse) return res.status(204).json(apiDeleteMedicalAppointmentResponse);
        // Enviamos el error.
        res.status(404).send({ message: error404Message });
    }
}