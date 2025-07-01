// Funcion asincrona para conectar con la base de datos
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
// Expresion regular para validar si el token recibido cumple con el formato correcto.
import { tokenRegex } from '../../utils/export/GenericRegex.mjs';
// Funcion para verificar el token recibido.
import { jwtValidator } from '../../services/jwt/validator/jwtValidator.mjs';
// Funcion asincrona para encontrar el id del usuario que ha iniciado sesion en base a su email.
import { checkIfUserExistsFunction } from '../../services/database/functions/mongoDbFunctions.mjs';
// Mensajes genericos
import { USER_COLLECTION_NAME, UNAUTHORIZED_401_MESSAGE, NOT_ACCEPTABLE_406_MESSAGE, FORBIDDEN_403_MESSAGE } from '../../config/GenericEnvConfig.mjs';
/** Middleware that verifies the Authorization header.
 * 
 * @param {Request} req - The Express request object.
 * @param {Response} res - The Express response object.
 * @param {NextFunction} next - The next middleware in the stack.
*/
export async function authMiddleware(req, res, next) {
    // Obtenemos el valor del encabezado mediante get() y el encabezado "Accept".
    const acceptHeader = req.get('Accept');
    // Verificamos que el tipo de contenido aceptado sea json mediante accepts() y el tipo de contenido "json".
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
    // Obtenemos del modelo el documento(el usuario completo) del token recibido en la request.
    const validationTokenResponse = await db.collection(USER_COLLECTION_NAME).findOne({ userJWT: token });
    // Si el token no se encunetra quiere decir que es incorrecto y devolvemos el error correspondiente.
    if (validationTokenResponse === null) return res.status(401).json({ message: `${UNAUTHORIZED_401_MESSAGE} El token es incorrecto.` });
    // Si el token recibido se encuentra en nuestra base de datos quiere decir que ese token pertenece a un usuario activo.
    // Verificamos el token para obtener el playload. Recibimos el email y el rol o null si la verificacion es incorrecta.
    const jwtValidation = jwtValidator(token);
    if (jwtValidation === null) return res.status(401).json({ message: UNAUTHORIZED_401_MESSAGE });
    // Utilizamos la funcion para obtener el id del usuario correspondiente con el email recibido en jwtValidation.userEmail
    // ya que es el email del usuario logueado.
    const loguedUser = await checkIfUserExistsFunction(jwtValidation.userEmail, { returnUser: true });
    // Guardamos los valores obtenidos del playload recibido(jwtValidation) en la request como un objeto.
    req.user = { userId: loguedUser._id, userRole: jwtValidation.userRole };
    // Enviamos la informacion la siguiente funcion que se ejecutara en la cola(next()).
    next();
    return;
}