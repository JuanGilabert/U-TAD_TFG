// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateUser, validatePartialUser } from '../../models/user/userModelValidator.mjs';
// Importamos los mensajes genericos.
import {
    OKEY_200_MESSAGE, CREATED_201_MESSAGE,
    FORBIDDEN_403_MESSAGE, CONFLICT_409_MESSAGE,
    INTERNAL_SERVER_ERROR_500_MESSAGE
} from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase UserController
export class UserController { // HAY QUE REVISAR TODOS LOS ERRORES QUE SE DEVUELVEN AQUI. SEE: MODELO. REALIZAR ANTES DE NADA EL JSDoc.
    constructor({ model }) {
        this.model = model;
        this.getAllUsers = this.getAllUsers.bind(this);
        this.getUserById = this.getUserById.bind(this);
        this.postUser = this.postUser.bind(this);
        this.putUser = this.putUser.bind(this);
        this.patchUser = this.patchUser.bind(this);
        this.deleteUser = this.deleteUser.bind(this);
    }
    // Funcion asincrona para obtener todos los usuarios.
    getAllUsers = async (req, res) => {
        const { userRole } = req.user;
        // Si el rol del usuario es diferente de admin, devolvemos un error.
        if (userRole !== 'admin') return res.status(403).json({ message: FORBIDDEN_403_MESSAGE });
        // Obtenemos del modelo los datos requeridos.
        const apiGetAllUsersResponse = await this.model.getAllUsers();
        // Enviamos el error con el mensaje correspondiente. REVISAR QUE ES ESTO, PORQUE SE DEVUELVE FALSE Y DEMAS.posible 200 sin usuarios que mostrar
        if (apiGetAllUsersResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        return res.status(200).json(apiGetAllUsersResponse);
    }
    // Funcion asincrona para obtener el perfil de un usuario para su posterior edicion.
    getUserById = async (req, res) => {
        // Obtenemos los valores de la peticion.
        let apiGetUserByIdResponse = "";
        const { id } = req.params;
        try {
            // Si eres un admin puedes ver el perfil de cualquier usuario.
            const { userRole } = req.user;
            if (userRole === 'admin') apiGetUserByIdResponse = await this.model.getUserProfile(id);
            // Solo el usuario logueado es quien puede ver su propio perfil a parte de los administradores.
            const { userId } = req.user;
            if (id === userId) apiGetUserByIdResponse = await this.model.getUserProfile(id);
            else return res.status(403).json({ message: `${FORBIDDEN_403_MESSAGE} No tienes permiso para ver este perfil.` });
            // Enviamos el error con el mensaje correspondiente.
            // REVISAR QUE ES ESTO, PORQUE SE DEVUELVE FALSE Y DEMAS. no existe el usuario que se quiere ver posible 410
            if (apiGetUserByIdResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
            // Enviamos la respuesta obtenida.
            return res.status(200).json(apiGetUserByIdResponse);
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        }
    }
    // Funcion asincrona para registrar/crear un nuevo usuario.
    postUser = async (req, res) => {
        console.log(req.body);
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateUser(req.body);
        console.log(result);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos del modelo los datos requeridos.
        try {
            const registerUserModelResponse = await this.model.postUser(result.data);
            // Enviamos el error obtenido. La operación de inserción no fue reconocida por MongoDB.
            if (!registerUserModelResponse)
                return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} Error al crear la el usuario.` });
            // Si el email existe devolvemos un error(conflict).
            if (registerUserModelResponse?.type === "Email")
                return res.status(409).json({ message: `${CONFLICT_409_MESSAGE} ${registerUserModelResponse?.message}` });
            console.log(registerUserModelResponse);
            // Enviamos la respuesta obtenida.
            return res.status(201).json({ message: CREATED_201_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error.message || error}` });
        }
    }
    // Funcion asincrona para actualizar un usuario.
    putUser = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateUser(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        //Obtenemos los valores de la peticion.
        let putUserByIdModelResponse = "";
        const { id } = req.params;
        const { userRole } = req.user;
        // Si eres un admin puedes actualizar el perfil de cualquier usuario.
        try {
            if (userRole === 'admin') putUserByIdModelResponse = await this.model.putUser({ id, user: result.data, userRole });
            // Solo el usuario logueado es quien puede actualizar su propio perfil a parte de los administradores.
            const { userId } = req.user;
            if (id === userId) putUserByIdModelResponse = await this.model.putUser({ id, user: result.data });
            else return res.status(403).json({ message: `${FORBIDDEN_403_MESSAGE} No tienes permiso para actualizar este perfil.` });
            // Enviamos el error con el mensaje correspondiente.
            if (putUserByIdModelResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
            // Enviamos la respuesta obtenida.
            return res.status(200).send({ message: OKEY_200_MESSAGE });
        } catch (error) {
            console.error(error);
            return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
        }
    }
    // Funcion asincrona para actualizar un usuario.
    patchUser = async (req, res) => {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialUser(req.body);
        if (result.error) return res.status(422).json({ error: result.error.message });
        // Obtenemos los valores de la peticion.
        let patchUserByIdModelResponse = "";
        const { id } = req.params;
        // Si eres un admin puedes actualizar el perfil de cualquier usuario.
        const { userRole } = req.user;
        if (userRole === 'admin') patchUserByIdModelResponse = await this.model.patchUser({ id, user: result.data });
        // Solo el usuario logueado es quien puede actualizar su propio perfil a parte de los administradores.
        const { userId } = req.user;
        if (id === userId) patchUserByIdModelResponse = await this.model.patchUser({ id, user: result.data });
        else return res.status(403).json({ message: `${FORBIDDEN_403_MESSAGE} No tienes permiso para actualizar este perfil.` });
        // Enviamos el error con el mensaje correspondiente.
        if (patchUserByIdModelResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        // Enviamos la respuesta obtenida.
        return res.status(200).json(patchUserByIdModelResponse);
    }
    // Funcion asincrona para eliminar un usuario.
    deleteUser = async (req, res) => {
        let deleteUserByIdModelResponse = "";
        const { id } = req.params;
        // Si eres un admin puedes eliminar el perfil de cualquier usuario.
        const { userRole } = req.user;
        if (userRole === 'admin') deleteUserByIdModelResponse = await this.model.deleteUser(id);
        // Solo el usuario logueado es quien puede eliminar su propio perfil a parte de los administradores.
        const { userId } = req.user;
        if (id === userId) deleteUserByIdModelResponse = await this.model.deleteUser(id);
        else return res.status(403).json({ message: `${FORBIDDEN_403_MESSAGE} No tienes permiso para eliminar este perfil.` });
        // Enviamos el error con el mensaje correspondiente.
        if (deleteUserByIdModelResponse === false) return res.status(500).json({ message: INTERNAL_SERVER_ERROR_500_MESSAGE });
        // Enviamos la respuesta obtenida.
        return res.status(200).json(deleteUserByIdModelResponse);
    }
}