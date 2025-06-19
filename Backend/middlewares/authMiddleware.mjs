// Funcion asincrona para conectar con la base de datos
import { connectDB } from '../services/database/connection/mongoDbConnection.mjs';
// Expresion regular para validar si el token recibido cumple con el formato correcto.
import { tokenRegex } from '../utils/export/GenericRegex.mjs';
// Funcion para verificar el token recibido.
import { jwtValidator } from '../services/jwt/validator/jwtValidator.mjs';
// Funcion asincrona para encontrar el id del usuario que ha iniciado sesion en base a su email.
import { findUserIdByEmailFunction } from '../services/database/functions/findUserIdByEmailFunction.mjs';
// Mensajes genericos
import { AUTH_COLLECTION_NAME, UNAUTHORIZED_401_MESSAGE, NOT_ACCEPTABLE_406_MESSAGE } from '../utils/export/GenericEnvConfig.mjs';
/** Middleware that verifies the JWT token in the Authorization header
 * and adds the user data to the request if the token is valid.
 * @param {Request} req - The Express request object.
 * @param {Response} res - The Express response object.
 * @param {NextFunction} next - The next middleware in the stack.
*/
export async function authMiddleware(req, res, next) {
    // Verificamos que el contenido aceptado sea json.
    const acceptHeader = req.get('Accept');
    if (!acceptHeader || !req.accepts('json')) return res.status(406).send({ message: NOT_ACCEPTABLE_406_MESSAGE });
    //// Si no hay cabecera indicamos que no tienes autorizacion.
    const authorizationHeader = req.header('Authorization');
    if (!authorizationHeader) return res.status(401).json({ message: "No tienes cabecera de autorizacion." });
    // Si el token no es correcto indicamos que no tienes autorizacion.
    const [scheme, token] = authorizationHeader.split(' ');
    if (scheme !== 'Bearer' || !token || !tokenRegex.test(token))
    return res.status(401).json({ message: "No hay token o tiene un formato incorrecto o el formato de la cabecera es incorrecto(Bearer token)." });  
    // Nos conectamos a la base de datos para comprobar que el token recibido en la request se encuentra en la base de datos.
    const db = await connectDB();
    const validationTokenResponse = await db.collection(AUTH_COLLECTION_NAME).findOne({ userJWT: token });
    // Si el token recibido se encuentra en nuestra base de datos quiere decir que ese token pertenece a un usuario activo.
    if (validationTokenResponse) {
        // Verificamos el token para obtener el playload. Recibimos el email y el rol o null si la verificacion es incorrecta.
        const jwtValidation = jwtValidator(token);
        if (jwtValidation === null) return res.status(401).json({ message: UNAUTHORIZED_401_MESSAGE });
        // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en jwtValidation.userEmail
        // ya que es el email del usuario logueado.
        const loguedUserId = await findUserIdByEmailFunction(jwtValidation.userEmail);
        // Guardamos los valores obtenidos del playload recibido(jwtValidation) en la request.
        req.user = { userId: loguedUserId, userRole: jwtValidation.userRole };
    } else {
        // Si el token no se encunetra quiere decir que es incorrecto y devolvemos el error correspondiente.
        return res.status(401).json({ message: UNAUTHORIZED_401_MESSAGE });
    }
    // Enviamos la informacion la siguiente funcion que se ejecutara en la cola(next()) como un objeto(req.user).
    next();
    return;
}