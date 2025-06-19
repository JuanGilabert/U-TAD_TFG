// Importamos los modelos/schemas
import { validateMedicament, validatePartialMedicament } from '../../models/health/medicament/medicamentModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class HealthMedicamentController {
    constructor({ model }) { this.model = model;
        this.getAllMedicaments = this.getAllMedicaments.bind(this);
        this.getMedicamentById = this.getMedicamentById.bind(this);
        this.getMedicamentExpirationDates = this.getMedicamentExpirationDates.bind(this);
        this.postMedicament = this.postMedicament.bind(this);
        this.putMedicament = this.putMedicament.bind(this);
        this.patchMedicament = this.patchMedicament.bind(this);
        this.deleteMedicament = this.deleteMedicament.bind(this);
    }
    //
    getAllMedicaments = async (req, res) => {
        // Verificamos que los valores de la peticion sean validos.
        let result = "";
        if (fechaCaducidadMedicamento !== "hasNoValue") result = await validatePartialMedicament({ fechaCaducidadMedicamento });
        if (result.error) return res.status(400).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { fechaCaducidadMedicamento = "hasNoValue" } = req.query;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllMedicamentsModelResponse = await this.model.getAllMedicaments(userId, fechaCaducidadMedicamento);
            // Enviamos el error o la respuesta obtenida.
            return getAllMedicamentsModelResponse === null ?
            res.status(404).json({ message: "No existen medicamentos." })
            : getAllMedicamentsModelResponse === false ?
            res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllMedicamentsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMedicamentById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMedicamentByIdModelResponse = await this.model.getMedicamentById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getMedicamentByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getMedicamentByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getMedicamentExpirationDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getMedicamentsExpirationDateModelResponse = await this.model.getMedicamentExpirationDates(userId);
            return !getMedicamentsExpirationDateModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getMedicamentsExpirationDateModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postMedicament = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const { userId } = req.user;
        try {
            const apiPostNewMedicamentResponse = await this.model.postMedicament({ medicament: result.data, userId });
            // Enviamos la respuesta obtenida.
            if (apiPostNewMedicamentResponse) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. Error al crear el medicamento.` });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putMedicament = async (req, res) => {
        // Validaciones del objeto a insertar.
        const result = await validateMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putMedicamentModelResponse = await this.model.putMedicament({ id, medicament: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putMedicamentModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchMedicament = async (req, res) => {
        // Validaciones del objeto a actualizar.
        const result = await validatePartialMedicament(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchMedicamentModelResponse = await this.model.patchMedicament({ id, medicament: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchMedicamentModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchMedicamentModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteMedicament = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const apiDeleteMedicamentResponse = await this.model.deleteMedicament(id, userId);
            // Enviamos la respuesta obtenida.
            if (apiDeleteMedicamentResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}