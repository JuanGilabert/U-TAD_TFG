// Modulos
import pkg from 'jsonwebtoken';
const { verify } = pkg;
import { promisify } from 'util';
/* const verifyAsync = promisify(verify); Usar promisify en versiones de jsonwebtoken < 9
ya que verify no es nativamente promisificado(no devuelve una promesa). */
// Modulos locales
import { JWT_SECRET_KEY } from '../../../utils/export/GenericEnvConfig.mjs';
////
export const jwtValidator = (token) => {
    try {
        const { userEmail } = verify(token, JWT_SECRET_KEY, { algorithms: ['HS256'] });
        return userEmail;
    } catch {
        return null;
    }
};
export const jwtValidatorOld = (token) => verify(token, JWT_SECRET_KEY, { algorithms: ['HS256'] })
    .then(({ userEmail }) => userEmail)// Devolvemos la propiedad creada en el payload del token(userEmail) cuando se genera.
    .catch(() => null);