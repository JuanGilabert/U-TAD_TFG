// Importamos los modelos/schemas
import { validateNewMedicament, validatePartialNewMedicament } from '../../models/healthModels/model/modelValidator.mjs';
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
export class HealthMedicamentController {
    constructor({ model }) { this.model = model;
        this.getAllMedicaments = this.getAllMedicaments.bind(this);
        this.getMedicamentById = this.getMedicamentById.bind(this);
        this.postNewMedicament = this.postNewMedicament.bind(this);
        this.putMedicament = this.putMedicament.bind(this);
        this.patchMedicament = this.patchMedicament.bind(this);
        this.deleteMedicament = this.deleteMedicament.bind(this);
    }
    //
    getAllMedicaments = async (req, res) => {
        //if (req.method !== 'GET') return res.status(405).send({ message: "Metodo no permitido para este recurso." });
        // Verificamos que no existan parametros en la peticion ni cuerpo en la misma.
        if (Object.keys(req.body).length > 0 || Object.keys(req.params).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // // Obtenemos del modelo los datos requeridos.
        const apiGetMedicamentsResponse = await this.model.getAllMedicaments(userId);
        // Enviamos el error.
        if (apiGetMedicamentsResponse === false) return res.status(404).send({ message: "No existen medicamentos." });
        // Enviamos la respuesta obtenida.
        res.json(apiGetMedicamentsResponse);
    }
    getMedicamentById = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const medicamentByNationalCodeResponse = await this.model.getMedicamentById(id, userId);
        // Enviamos la respuesta obtenida.
        if (medicamentByNationalCodeResponse) return res.json(medicamentByNationalCodeResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: error404Message });
    }
    postNewMedicament = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewMedicament(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const apiPostNewMedicamentResponse = await this.model.postNewMedicament({ ...result.data, userId });
        if (apiPostNewMedicamentResponse) return res.status(201).send({ message: okey201Message });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: `${error500Message}. Error al crear el medicamento.` });
    }
    putUpdateMedicament = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar.
        const result = validateNewMedicament(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateMedicamentResponse = await this.model.putMedicament({ id, ...result.data, userId });
        // Enviamos el mensaje de error.
        if (apiPutUpdateMedicamentResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ messsage: okey200Message });
    }
    patchUpdateMedicament = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Si el codigo nacional es incorrecto o no existe devolvemos un error
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a actualizar.
        const result = validatePartialNewMedicament(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateMedicamentResponse = await this.model.patchMedicament({ id, ...result.data, userId });
        // Enviamos el mensaje de error.
        if (apiPatchUpdateMedicamentResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.json(apiPatchUpdateMedicamentResponse);
    }
    deleteMedicament = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida
        const apiDeleteMedicamentResponse = await this.model.deleteMedicament(id, userId);
        if (apiDeleteMedicamentResponse) return res.status(204).json(apiDeleteMedicamentResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: error404Message });
    }
}