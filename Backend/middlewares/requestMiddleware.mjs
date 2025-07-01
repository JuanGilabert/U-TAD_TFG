// 
import { BAD_REQUEST_400_MESSAGE, BAD_REQUEST_400_QUERY_MESSAGE } from '../config/GenericEnvConfig.mjs';
// Expresion regular para validar si el id recibido cumple con el formato correcto.
import { randomUUIDv4Regex } from '../utils/export/GenericRegex.mjs';
//
export async function requestMiddleware(req, res, next) {
  // Verificamos que el contenido aceptado sea json.
  const acceptHeader = req.get('Accept');
  if (!acceptHeader || !req.accepts('json')) return res.status(406).json({ message: NOT_ACCEPTABLE_406_MESSAGE });
  //// Verificamos el tipo de metodo que hace la request para validar los valores de la peticion como los params, query o body.
  if (req.method === 'GET') {
    // Si hay body devolvemos el error correspondiente.
    if (req.body) return res.status(400).json({ message: `${BAD_REQUEST_400_MESSAGE} No se admite un cuerpo en este tipo de peticion.` });
    // Si la ruta tiene params, verificamos que no existan queries en la peticion para comprobar la ruta /api/endpoint/:id en este caso.
    if (Object.keys(req.params).length > 0) {
      if (Object.keys(req.query).length > 0) return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
      // Validaciones de los parametros recibidos.
      if (!checkParamsFunction(req.params.id)) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
    }
    // Si no hay body y tampoco hay params entonces accedemos a /api/endpoint donde si puede haber queries en la peticion al ser un get a la barra /.
  }
  if (req.method === 'POST') {
    // Verificamos que exista un body en la request.
    if (!req.body) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
    // Verificamos que no haya queries en la peticion.
    if (Object.keys(req.query).length > 0) return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
  }
  if (req.method === 'PUT' || req.method === 'PATCH') {
    // Verificamos que no haya queries en la peticion.
    if (Object.keys(req.query).length > 0) return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Verificamos que haya cuerpo en la peticion y que hayan parametros.
    if (!req.body || !Object.keys(req.params).length) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
    // Validaciones de los parametros recibidos.
    if (!checkParamsFunction(req.params.id)) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
  }
  if (req.method === 'DELETE') {
    // Verificamos que no exista cuerpo en la peticion.
    if (req.body) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
    // Validaciones de los parametros recibidos.
    if (!checkParamsFunction(req.params.id)) return res.status(400).json({ message: BAD_REQUEST_400_MESSAGE });
  }
  //if (req.method === 'OPTIONS' || req.method === 'HEAD') return next();
  // Continuamos con el siguiente handler en la cola.
  next();
}
// Validamos que el id sea valido, es decir que el id sea un string randomUUID de version 4.
function checkParamsFunction(id) { return !(!id || !randomUUIDv4Regex.test(id)); }