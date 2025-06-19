// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateFood, validatePartialFood } from '../../models/food/foodModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    NOT_FOUND_404_MESSAGE, NOT_FOUND_404_QUERY_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class FoodController {
    constructor({ model }) {
        this.model = model;
        this.getAllFoods = this.getAllFoods.bind(this);
        this.getFoodById = this.getFoodById.bind(this);
        this.getFoodUnavailableDates = this.getFoodUnavailableDates.bind(this);
        this.postFood = this.postFood.bind(this);
        this.putFood = this.putFood.bind(this);
        this.patchFood = this.patchFood.bind(this);
        this.deleteFood = this.deleteFood.bind(this);
    }
    //
    getAllFoods = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        const { tipoComida = "hasNoValue", fechaReserva = "hasNoValue" } = req.query;
        // Verificamos que los valores de la peticion sean validos.
        let result = "";
        if (tipoComida !== "hasNoValue") result = await validatePartialFood({ tipoComida });
        if (fechaReserva !== "hasNoValue") result = await validatePartialFood({ fechaReserva });
        if (result.error) return res.status(400).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        try {
            const getAllFoodsModelResponse = await this.model.getAllFoods(userId, tipoComida, fechaReserva);
            // Enviamos el error o la respuesta obtenida.
            return getAllFoodsModelResponse === null ?
            res.status(404).json({ message: "Todavia no existe ninguna cita." })
            : getAllFoodsModelResponse === false ?
            res.status(404).json({ message: NOT_FOUND_404_QUERY_MESSAGE })
            : res.status(200).json(getAllFoodsModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() entre otros.
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
        return res.status(200).json(getAllFoodsModelResponse);
    }
    getFoodById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getFoodByIdModelResponse = await this.model.getFoodById(id, userId);
            // Enviamos el error o la respuesta obtenida.
            return !getFoodByIdModelResponse ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(getFoodByIdModelResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    getFoodUnavailableDates = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const getFoodUnavailableDatesModelResponse = await this.model.getFoodUnavailableDates(userId);
            return !getFoodUnavailableDatesModelResponse ?
            res.status(404).json({ message: "No existen fechas de reserva no disponibles." })
            : res.status(200).json(getFoodUnavailableDatesModelResponse);
        } catch (error) {
            // Posible excepcion causada por el metodo .toArray() o por el metodo aggregate([]).
            console.error(error);
            // Enviamos el error obtenido.
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    postFood = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const { userId } = req.user;
        try {
            const postNewFoodModelResult = await this.model.postFood({ food: result.data, userId });
            // Enviamos la respuesta obtenida.
            if (postNewFoodModelResult) return res.status(201).json({ message: CREATED_201_MESSAGE });
            // Enviamos el error obtenido.
            return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    putFood = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const putFoodModelResponse = await this.model.putFood({ id, food: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return putFoodModelResponse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    patchFood = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const patchFoodModelRespnse = await this.model.patchFood({ id, food: result.data, userId });
            // Enviamos el error o la respuesta obtenida.
            return patchFoodModelRespnse === false ? res.status(404).json({ message: NOT_FOUND_404_MESSAGE })
            : res.status(200).json(patchFoodModelRespnse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    deleteFood = async (req, res) => {
        // Obtenemos los valores de la peticion.
        const { id } = req.params;
        const { userId } = req.user;
        // Obtenemos del modelo los datos requeridos.
        try {
            const deleteFoodModelResponse = await this.model.deleteFood(id, userId);
            // Enviamos la respuesta obtenida.
            if (deleteFoodModelResponse) return res.status(204).send();
            // Enviamos el error obtenido.
            return res.status(404).json({ message: NOT_FOUND_404_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
}