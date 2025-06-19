// Modulos de node
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
        const { userEmail, userRole } = verify(token, JWT_SECRET_KEY, { algorithms: ['HS256'] });
        return { userEmail, userRole };
    } catch {
        return null;
    }
};