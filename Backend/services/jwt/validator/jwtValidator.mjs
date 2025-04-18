// Modulos
//import { verify } from "jsonwebtoken";
import { promisify } from 'util';
//
import pkg from 'jsonwebtoken';
const { verify } = pkg;
/* const verifyAsync = promisify(verify); Usar promisify en versiones de jsonwebtoken < 9
ya que verify no es nativamente promisificado(no devuelve una promesa). */
////
export const jwtValidator = (token) => verify(token, process.env.JWT_SECRET, { algorithms: ['HS256'] })
    .then(({ userEmail }) => userEmail)// Devolvemos la propiedad creada en el payload del token(userEmail) cuando se genera.
    .catch(() => null);