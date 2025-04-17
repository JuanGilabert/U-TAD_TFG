import cookieParser from 'cookie-parser'; //REVISAR MIDUDEV
// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateNewUser, validatePartialNewUser } from '../../models/authModels/authModelValidator.mjs';
import { findUserIdByEmailFunction } from '../../utils/functions/findUserIdByEmailFunction.mjs';
//// Constantes a nivel de modulo.
const okey200Message = process.env.OKEY_200_MESSAGE;
const okey201Message = process.env.CREATED_201_MESSAGE;
const error400Message = process.env.BAD_REQUEST_400_MESSAGE;
const error401Message = process.env.UNAUTHORIZED_401_MESSAGE;
const error404Message = process.env.NOT_FOUND_404_MESSAGE;
const error406Message = process.env.NOT_ACCEPTABLE_406_MESSAGE;
const error409Message = process.env.CONFLICT_409_MESSAGE;
const error500Message = process.env.INTERNAL_SERVER_ERROR_500_MESSAGE;
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
        // Verificamos que el contenido aceptado sea json.
        if (!req.accepts('json')) return res.status(406).json({ error: error406Message });
        // Verificamos que no haya queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion.
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validateNewUser(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const registerUserModelResponse = await this.model.postNewUser({ ...result.data });
        if (registerUserModelResponse?.message === "Existing email.")
            return res.status(409).json({ message: `${error409Message}. ${registerUserModelResponse.message}` });
        if (registerUserModelResponse?.message === "Error creating user.")
            return res.status(500).json({ message: `${error500Message}. ${registerUserModelResponse.message}.` });
        // Enviamos la respuesta obtenida
        res.status(201).json({ message: okey201Message });
    }
    // Funcion asincrona para loguear un usuario
    postLoginUser = async (req, res) => {
        // Verificamos que el contenido aceptado sea json.
        if (!req.accepts('json')) return res.status(406).json({ error: error406Message });
        // Verificamos que no haya queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros en la peticion
        if (Object.keys(req.params).length > 0) return res.stauts(400).send({ message: error400Message });
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = validatePartialNewUser(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        const apiLoginUserResponse = await this.model.postLoginUser({ ...result.data });
        // Si el email o la contraseña son incorrectos devolvemos el error correspondiente.
        if (apiLoginUserResponse?.message === "Invalid input.")
            return res.status(401).json({ message: `${error401Message}. El email o la contraseña son incorrectos.` });
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (apiLoginUserResponse?.message === "Login error." )
            return res.status(500).json({ message: `${error500Message}. ${apiLoginUserResponse.message}.` });
        // Si el usuario no ha podido actaulizarse en la ddbb entonces devolvemos el error.
        if (apiLoginUserResponse === false) return res.status(500).json({ message: error500Message });
        /* Enviamos la respuesta obtenida, es decir el token.
        La cookie no puede ser accedida desde el lado del cliente,
        solo funciona con https y solo es accesible en el mismo dominio. */
        res.cookie('token', apiLoginUserResponse,
            { httpOnly: true, secure: true, sameSite: 'strict' }
        ).json(apiLoginUserResponse);
    }
    // Funcion asincrona para desloguear un usuario
    postLogoutUser = async (req, res) => {
        // Verificamos que el contenido aceptado sea json.
        if (!req.accepts('json')) return res.status(406).json({ error: error406Message });
        // Verificamos que no haya queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros ni cuerpo en la peticion.
        if (Object.keys(req.params).length > 0 || Object.keys(req.body).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos recibidos.
        const apiLogoutUserResponse = await this.model.postLogoutUser(userId);
        // Si ocurre un error interno al comparar las contraseñas o al generar el token devolvemos el error correspondiente.
        if (apiLogoutUserResponse === false) return res.status(500).send({ message: `${error500Message}. No se pudo cerrar la sesion.` });
        // Enviamos la respuesta obtenida.
        res.status(200).send({ message: okey200Message });
    }
    // Funcion asincrona para obtener el perfil de un usuario para su edicion
    getUserProfileData = async (req, res) => {
        // Verificamos que el contenido aceptado sea json.
        if (!req.accepts('json')) return res.status(406).json({ error: error406Message });
        // Verificamos que no haya queries en la peticion.
        if (Object.keys(req.query).length > 0) return res.status(400).json({ error: errorQueryMessage });
        // Verificamos que no existan parametros ni cuerpo en la peticion.
        if (Object.keys(req.params).length > 0 || Object.keys(req.body).length > 0)
            return res.stauts(400).send({ message: error400Message });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en req.user.userEmail.
        const userId = findUserIdByEmailFunction(req.user.userEmail);
        // Obtenemos del modelo los datos recibidos.
        const apiGetProfileResponse = await this.model.getUserProfileData(userId);
        // Enviamos el error.
        if (apiGetProfileResponse === false) return res.status(500).send({ message: error500Message });
        // Enviamos la respuesta obtenida.
        res.json(apiGetProfileResponse);
    }
}