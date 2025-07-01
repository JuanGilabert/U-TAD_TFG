// Modulos
//import { promisify } from 'util'; NOT-USED.
import pkg from 'jsonwebtoken';
const { sign } = pkg;
// Modulos locales
import { JWT_SECRET_KEY } from '../../../config/GenericEnvConfig.mjs';
/**
 * Generates a JSON Web Token (JWT) for a given user email.
 * @param {Object} param - An object containing user information.
 * @param {string} param.userEmail - The email of the user for whom the token is generated.
 * @returns {string} The generated JWT, which expires in 1 hour.
 */
export const jwtGenerator = ({ userEmail, userRole }) => {
    return sign({ userEmail: userEmail, userRole: userRole }, JWT_SECRET_KEY);
};
export const jwtGenerator2 = (jwtArgs) => { return sign({ ...jwtArgs }, JWT_SECRET_KEY); };