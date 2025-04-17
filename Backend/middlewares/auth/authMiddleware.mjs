// Funcion para verificar el token.
import { jwtValidator } from '../../services/jwt/validator/jwtValidator.mjs';
// Funcion asincrona para conectar con la base de datos
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
// Expresion regular para validar si el token recibido cumple con el formato correcto.
import { tokenRegex } from '../../utils/export/GenericRegex.mjs';
// Variables de entorno usadas en el middleware.
const error401Message = `${process.env.UNAUTHORIZED_401_MESSAGE}. El token es incorrecto.`;
const authCollectionName = process.env.AUTH_COLLECTION_NAME;
////
/** Middleware that verifies the JWT token in the Authorization header
 * and adds the user data to the request if the token is valid.
 * @param {Request} req - The Express request object.
 * @param {Response} res - The Express response object.
 * @param {NextFunction} next - The next middleware in the stack.
*/
export async function authMiddleware(req, res, next) {
  // Si no hay cabecera indicamos que no tienes autorizacion.
  const authorizationHeader = req.header('Authorization');
  if (!authorizationHeader) return res.status(401).json({ message: "No tienes cabecera de autorizacion." });
  // Si el token no es correcto indicamos que no tienes autorizacion.
  const [scheme, token] = authorizationHeader.split(' '); // Formato: (Bearer token).
  if (scheme !== 'Bearer' || !token || !tokenRegex.test(token))
    return res.status(401).json({ message: "No hay token o el formato es incorrecto." });  
  // Nos conectamos a la base de datos para comprobar que el token recibido en la request se encuentra en la base de datos.
  const db = connectDB();
  const validationTokenResponse = await db.collection(authCollectionName).findOne({ userJWT: token }, { projection: { _id: 0 } });
  // Si el token recibido se encuentra en nuestra base de datos quiere decir que ese token pertenece a un usuario activo.
  if (validationTokenResponse) {
    // Verificamos el token para obtener el playload. Recibimos el email o null si el token es incorrecto.
    const jwtValidation = jwtValidator(token);
    if (jwtValidation === null) return res.status(401).json({ message: error401Message });
    // Guardamos del playload recibido(jwtValidation) el valor del email obtenido de la funcion.
    req.user = { userEmail: jwtValidation };
    // Enviamos la informacion la siguiente funcion que se ejecutara en la cola(next()) como un objeto(req.user).
    next();
    return;
  }
  // Si el token no se encunetra quiere decir que es incorrecto y devolvemos el error correspondiente.
  return res.status(401).json({ message: error401Message });
}
