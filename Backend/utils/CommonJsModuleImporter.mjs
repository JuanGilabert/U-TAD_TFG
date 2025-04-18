/* Lector de ficheros JSON utilizado en los modelos/schemas para leer los json(con datos) asociados a dichos modelos. */
// Importamos la funcion  createRequire  para crear modulos de los ficheros .json relativos a los modelos/schemas.
import { createRequire } from 'node:module';
// import.meta.url  :  define/contiene la ruta actual del proyecto (taskManagerServer).
const require = createRequire(import.meta.url);
// Cuando esta funcion sea ejecutada devolvera el require de la ruta recibida por parametro.
export const commonJSModule = (path) => require(path)