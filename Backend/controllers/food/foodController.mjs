// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewFood, validatePartialNewFood } from '../../models/food/foodModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, NOT_FOUND_404_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
export class FoodController {
    constructor({ model }) {
        this.model = model;
        this.getAllFoods = this.getAllFoods.bind(this);
        this.getFoodById = this.getFoodById.bind(this);
        this.postNewFood = this.postNewFood.bind(this);
        this.putUpdateFood = this.putUpdateFood.bind(this);
        this.patchUpdateFood = this.patchUpdateFood.bind(this);
        this.deleteFood = this.deleteFood.bind(this);
    }
    //
    getAllFoods = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllFoodsModelResponse = await this.model.getAllFoods(userId);
        // Enviamos el mensaje de error.
        if (getAllFoodsModelResponse === false) return res.status(404).send({ message: "No existen citas." });
        // Enviamos la respuesta obtenida.
        res.status(200).json(getAllFoodsModelResponse);
    }
    getFoodById = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const getFoodByIdModelResponse = await this.model.getFoodById(id, userId);
        if (getFoodByIdModelResponse) return res.status(200).json(getFoodByIdModelResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
    postNewFood = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const postNewFoodModelResult = await this.model.postNewFood({ food: result.data, userId });
        if (postNewFoodModelResult) return res.status(201).send({ message: CREATED_201_MESSAGE });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
    }
    putUpdateFood = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdateFoodModelResponse = await this.model.putUpdateFood({ id, food: result.data, userId });
        // Enviamos el mensaje de error.
        if (putUpdateFoodModelResponse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: OKEY_200_MESSAGE });
    }
    patchUpdateFood = async (req, res) => {
        const { id } = req.params;
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialNewFood(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const patchUpdateFoodModelRespnse = await this.model.patchUpdateFood({ id, food: result.data, userId });
        // Enviamos el mensaje de error.
        if (patchUpdateFoodModelRespnse === false) return res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).json(patchUpdateFoodModelRespnse);
    }
    deleteFood = async (req, res) => {
        const { id } = req.params;
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = await findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deleteFoodModelResponse = await this.model.deleteFood(id, userId);
        if (deleteFoodModelResponse) return res.status(204).json(deleteFoodModelResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: NOT_FOUND_404_MESSAGE });
    }
}