// Importamos los modelos/schemas
import { validateNewMedicament, validatePartialNewMedicament } from '../../models/health/medicament/medicamentModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class HealthMedicamentController {
    constructor({ model }) { this.model = model;
        this.getAllMedicaments = this.getAllMedicaments.bind(this);
        this.getMedicamentById = this.getMedicamentById.bind(this);
        this.getMedicamentAvailableDates = this.getMedicamentAvailableDates.bind(this);
        this.postNewMedicament = this.postNewMedicament.bind(this);
        this.putUpdateMedicament = this.putUpdateMedicament.bind(this);
        this.patchUpdateMedicament = this.patchUpdateMedicament.bind(this);
        this.deleteMedicament = this.deleteMedicament.bind(this);
    }
    //
    getAllMedicaments = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // // Obtenemos del modelo los datos requeridos.
        const apiGetMedicamentsResponse = await this.model.getAllMedicaments(userId);
        // Enviamos el error.
        if (apiGetMedicamentsResponse === false) return res.status(404).send({ message: "No existen medicamentos." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(apiGetMedicamentsResponse);
    }
    getMedicamentById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const medicamentByNationalCodeResponse = await this.model.getMedicamentById(id, userId);
        // Enviamos la respuesta obtenida.
        if (medicamentByNationalCodeResponse) return res.status(200).json(medicamentByNationalCodeResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    getMedicamentExpirationDates = async (req, res) => {
        const { fechaCaducidadMedicamento = "hasNoValue" } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos enviando el ususario que solicita los datos.
        const getMedicamentsExpirationDateModelResponse = await this.model.getMedicamentExpirationDates(userId, fechaCaducidadMedicamento);
        // Enviamos el error.
        if (getMedicamentsExpirationDateModelResponse === false)
            return res.status(404).send({ message: "No existen fechas de caducidad de medicamentos." });
        if (getMedicamentsExpirationDateModelResponse?.message === "medicamentExpirationDatesError")
            return res.status(404).send({ message: "No existen fechas de caducidad para esta fecha." });
        // Enviamos la respuesta obtenida.
        return res.status(200).json(getMedicamentsExpirationDateModelResponse);
    }
    postNewMedicament = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const apiPostNewMedicamentResponse = await this.model.postNewMedicament({ medicament: result.data, userId });
        if (apiPostNewMedicamentResponse) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear el medicamento.` });
    }
    putUpdateMedicament = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar.
        const result = await validateNewMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPutUpdateMedicamentResponse = await this.model.putMedicament({ id, medicament: result.data, userId });
        // Enviamos el mensaje de error.
        if (apiPutUpdateMedicamentResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ messsage: OKEY_200_MESSAGE });
    }
    patchUpdateMedicament = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a actualizar.
        const result = await validatePartialNewMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const apiPatchUpdateMedicamentResponse = await this.model.patchMedicament({ id, medicament: result.data, userId });
        // Enviamos el mensaje de error.
        if (apiPatchUpdateMedicamentResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).json(apiPatchUpdateMedicamentResponse);
    }
    deleteMedicament = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida
        const apiDeleteMedicamentResponse = await this.model.deleteMedicament(id, userId);
        if (apiDeleteMedicamentResponse) return res.status(204).json(apiDeleteMedicamentResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}