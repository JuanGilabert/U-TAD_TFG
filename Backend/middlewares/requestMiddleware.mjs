// Funcion para verificar el token.
import { jwtValidator } from '../services/jwt/validator/jwtValidator.mjs';
// Funcion asincrona para conectar con la base de datos
import { connectDB } from '../services/database/connection/mongoDbConnection.mjs';
import { AUTH_COLLECTION_NAME, BAD_REQUEST_400_MESSAGE, BAD_REQUEST_400_QUERY_MESSAGE, UNAUTHORIZED_401_MESSAGE,
  NOT_ACCEPTABLE_406_MESSAGE
} from '../utils/export/GenericEnvConfig.mjs';
// Expresion regular para validar si el token recibido cumple con el formato correcto.
import { tokenRegex } from '../utils/export/GenericRegex.mjs';
// Expresion regular para validar si el id recibido cumple con el formato correcto.
import { randomUUIDv4Regex } from '../utils/export/GenericRegex.mjs';
/** Middleware that verifies the JWT token in the Authorization header
 * and adds the user data to the request if the token is valid.
 * @param {Request} req - The Express request object.
 * @param {Response} res - The Express response object.
 * @param {NextFunction} next - The next middleware in the stack.
*/
export async function requestMiddleware(req, res, next) {
  // Verificamos que el contenido aceptado sea json.
  const acceptHeader = req.get('Accept');
  if (!acceptHeader || !req.accepts('json')) return res.status(406).send({ message: NOT_ACCEPTABLE_406_MESSAGE });
  //// Verificamos el tipo de metodo que hace la request para validar los valores de la peticion como los params, query o body.
  if (req.method === 'GET') {
    // Si hay body devolvemos el error correspondiente.
    if (req.body) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
    // Si la ruta tiene params, verificamos que no existan queries en la peticion para comprobar la ruta /api/endpoint/:id en este caso.
    if (Object.keys(req.params).length > 0) {
      if (Object.keys(req.query).length > 0) return res.status(400).send({ message: BAD_REQUEST_400_QUERY_MESSAGE });
      // Validaciones de los parametros recibidos.
      if (!checkParamsFunction(req.params.id)) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
    }
    // Si no hay body y tampoco hay params accedemos a /api/endpoint
  }
  if (req.method === 'POST') {
    // Verificamos que no haya queries en la peticion.
    if (Object.keys(req.query).length > 0) return res.status(400).send({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Verificamos que no existan parametros en la peticion y que exista body.
    if (!req.body || Object.keys(req.params).length > 0) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
  }
  if (req.method === 'PUT' || req.method === 'PATCH') {
    // Verificamos que no haya queries en la peticion.
    if (Object.keys(req.query).length > 0) return res.status(400).send({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Verificamos que haya cuerpo en la peticion y que hayan parametros.
    if (!req.body || Object.keys(req.params).length === 0) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
    // Validaciones de los parametros recibidos.
    if (!checkParamsFunction(req.params.id)) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
  }
  if (req.method === 'DELETE') {
    // Verificamos que no haya queries en la peticion.
    if (Object.keys(req.query).length > 0) return res.status(400).send({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Verificamos que no exista cuerpo en la peticion.
    if (req.body) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
    // Validaciones de los parametros recibidos.
    if (!checkParamsFunction(req.params.id)) return res.status(400).send({ message: BAD_REQUEST_400_MESSAGE });
  }
  //if (req.method === 'OPTIONS' || req.method === 'HEAD') return next();
  //// Si no hay cabecera indicamos que no tienes autorizacion.
  const authorizationHeader = req.header('Authorization');
  if (!authorizationHeader) return res.status(401).json({ message: "No tienes cabecera de autorizacion." });
  // Si el token no es correcto indicamos que no tienes autorizacion.
  const [scheme, token] = authorizationHeader.split(' ');
  if (scheme !== 'Bearer' || !token || !tokenRegex.test(token))
    return res.status(401).json({ message: "No hay token o esta mal formado o el formato es incorrecto(Bearer token)." });  
  //// Nos conectamos a la base de datos para comprobar que el token recibido en la request se encuentra en la base de datos.
  const db = await connectDB();
  const validationTokenResponse = await db.collection(AUTH_COLLECTION_NAME).findOne({ userJWT: token });
  // Si el token recibido se encuentra en nuestra base de datos quiere decir que ese token pertenece a un usuario activo.
  if (validationTokenResponse) {
    // Verificamos el token para obtener el playload. Recibimos el email o null si el token es incorrecto.
    const jwtValidation = jwtValidator(token);
    if (jwtValidation === null) return res.status(401).json({ message: UNAUTHORIZED_401_MESSAGE });
    // Guardamos del playload recibido(jwtValidation) el valor del email obtenido de la funcion.
    req.user = { userEmail: jwtValidation };
    // Enviamos la informacion la siguiente funcion que se ejecutara en la cola(next()) como un objeto(req.user).
    next();
    return;
  }
  // Si el token no se encunetra quiere decir que es incorrecto y devolvemos el error correspondiente.
  return res.status(401).json({ message: UNAUTHORIZED_401_MESSAGE });
}
function checkParamsFunction(id) {
  // Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
  if (!id || !randomUUIDv4Regex.test(id)) return false;
  return true;
}