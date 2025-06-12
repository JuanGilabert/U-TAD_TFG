// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewUser, validateLoginUser, validateLogoutUser } from '../../models/auth/authModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../services/database/functions/findUserIdByEmailFunction.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, UNAUTHORIZED_401_MESSAGE, CONFLICT_409_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
////
export class AuthController {
    constructor({ model }) {
        this.model = model;
        this.postNewUser = this.postNewUser.bind(this);
        this.postLoginUser = this.postLoginUser.bind(this);
        this.postLogoutUser = this.postLogoutUser.bind(this);
        this.getUserProfileData = this.getUserProfileData.bind(this);
    }
    // Funcion asincrona para registrar/crear un nuevo usuario.
    postNewUser = async (req, res) => {
        console.log(req.body);
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateNewUser(req.body);
        console.log(result);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const registerUserModelResponse = await this.model.postNewUser(result.data);
        // Si el email existe devolvemos un error(conflict).
        if (registerUserModelResponse?.message === "Existing email.")
            return res.status(409).json({ message: `${CONFLICT_409_MESSAGE}. ${registerUserModelResponse.message}` });
        if (registerUserModelResponse?.message === "Error creating user.")
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. ${registerUserModelResponse.message}.` });
        console.log(registerUserModelResponse);
        // Enviamos la respuesta obtenida.
        res.status(201).json({ message: CREATED_201_MESSAGE });
    }
    // Funcion asincrona para loguear un usuario.
    postLoginUser = async (req, res) => {
        console.log(req.body);
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateLoginUser(req.body);
        console.log(result);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const apiLoginUserResponse = await this.model.postLoginUser(result.data);
        // Si el email o la contraseña son incorrectos devolvemos el error correspondiente.
        if (apiLoginUserResponse?.message === "Invalid input.")
            return res.status(401).json({ message: `${UNAUTHORIZED_401_MESSAGE} El email o la contraseña son incorrectos.` });
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (apiLoginUserResponse?.message === "Login error.")
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. ${apiLoginUserResponse.message}.` });
        // Si el usuario no ha podido actaulizarse en la ddbb entonces devolvemos el error.
        if (apiLoginUserResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        console.log(apiLoginUserResponse)
        /* Enviamos la respuesta obtenida, es decir el token.
        La cookie no puede ser accedida desde el lado del cliente,
        solo funciona con https y solo es accesible en el mismo dominio. */
        res.status(200).json({ token: apiLoginUserResponse});
    }
    // Funcion asincrona para desloguear un usuario
    postLogoutUser = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateLogoutUser(req.body);
        if (result.error) return res.status(422).send({ error: result.error.message });
        // Obtenemos del modelo los datos recibidos.
        const apiLogoutUserResponse = await this.model.postLogoutUser(result.data);
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (apiLogoutUserResponse === false) return res.status(500).send({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se pudo cerrar la sesion.` });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: "La sesion se ha cerrado correctamente." });
    }
    // Funcion asincrona para obtener el perfil de un usuario para su edicion
    getUserProfileData = async (req, res) => {
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos recibidos.
        const apiGetProfileResponse = await this.model.getUserProfileData(userId);
        // Enviamos el error.
        if (apiGetProfileResponse === false) return res.status(500).send({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        // Enviamos la respuesta obtenida.
        res.status(200).json(apiGetProfileResponse);
    }
}