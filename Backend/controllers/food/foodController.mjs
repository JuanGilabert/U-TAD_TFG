// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewFood, validatePartialNewFood } from '../../models/foodModels/foodModelValidator.mjs';
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
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0 || Object.keys(req.body).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const getAllFoodsModelResponse = await this.model.getAllFoods(userId);
        // Enviamos el mensaje de error.
        if (getAllFoodsModelResponse === false) return res.status(404).send({ message: "No existen citas." });
        // Enviamos la respuesta obtenida.
        res.json(getAllFoodsModelResponse);
    }
    getFoodById = async (req, res) => {
        // Verificamos que no haya cuerpo ni queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: this.error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: this.error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos. Enviamos la respuesta obtenida.
        const getFoodByIdModelResponse = await this.model.getFoodById(id, userId);
        if (getFoodByIdModelResponse) return res.json(getFoodByIdModelResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: this.error404Message });
    }
    postNewFood = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewFood(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const postNewFoodModelResult = await this.model.postNewFood({ ...result.data, userId });
        if (postNewFoodModelResult) return res.status(201).send({ message: okey201Message });
        // Enviamos el mensaje de error.
        res.status(500).send({ message: error500Message });
    }
    putUpdateFood = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewFood(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const putUpdateFoodModelResponse = await this.model.putUpdateFood({ id, ...result.data, userId });
        // Enviamos el mensaje de error.
        if (putUpdateFoodModelResponse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: okey200Message });
    }
    patchUpdateFood = async (req, res) => {
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewFood(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const patchUpdateFoodModelRespnse = await this.model.patchUpdateFood({ id, ...result.data, userId });
        // Enviamos el mensaje de error.
        if (patchUpdateFoodModelRespnse === false) return res.status(404).send({ message: error404Message });
        // Enviamos la respuesta obtenida.
        res.json(apiPatchUpdateCinemaResponse);
    }
    deleteFood = async (req, res) => {
        // Verificamos que no haya cuerpo ni queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        if (Object.keys(req.body).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones de los parametros recibidos.
        const { id } = req.params;
        if (!id || !randomUUIDv4Regex.test(id) || Object.keys(req.params).length > 1)
            return res.status(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos requeridos.
        const deleteFoodModelResponse = await this.model.deleteFood(id, userId);
        if (deleteFoodModelResponse) return res.status(204).json(deleteFoodModelResponse);
        // Enviamos el mensaje de error.
        res.status(404).send({ message: error404Message });
    }
}