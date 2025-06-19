// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateSignInUser, validateSignOutUser } from '../../models/auth/authModelValidator.mjs';
// Importamos los mensajes genericos.
import { OKEY_200_MESSAGE, CREATED_201_MESSAGE, UNAUTHORIZED_401_MESSAGE, INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
////
export class AdminController {
    constructor({ model }) {
        this.model = model;
        this.postSignInUser = this.postSignInUser.bind(this);
        this.postSignOutUser = this.postSignOutUser.bind(this);
    }
    // Funcion asincrona para loguear un usuario.
    postSignInUser = async (req, res) => {
        console.log(req.body);
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateSignInUser(req.body);
        console.log(result);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const signinModelResponse = await this.model.postSignInUser(result.data);
        // Si el email o la contraseña son incorrectos devolvemos el error correspondiente.
        if (signinModelResponse?.message === "Invalid input.")
            return res.status(401).json({ message: `${UNAUTHORIZED_401_MESSAGE} El email o la contraseña son incorrectos.` });
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (signinModelResponse?.message === "Login error.")
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. ${signinModelResponse.message}.` });
        // Si el usuario no ha podido actaulizarse en la ddbb entonces devolvemos el error.
        if (signinModelResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        console.log(signinModelResponse)
        /* Enviamos la respuesta obtenida, es decir el token.
        La cookie no puede ser accedida desde el lado del cliente,
        solo funciona con https y solo es accesible en el mismo dominio. */
        return res.status(200).json({ token: signinModelResponse});
    }
    // Funcion asincrona para desloguear un usuario
    postSignOutUser = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateSignOutUser(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos recibidos.
        const signoutModelResponse = await this.model.postSignOutUser(result.data);
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (signoutModelResponse === false) return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE}. No se pudo cerrar la sesion.` });
        // Enviamos la respuesta obtenida.
        return res.status(200).json({ message: "La sesion se ha cerrado correctamente." });
    }
}