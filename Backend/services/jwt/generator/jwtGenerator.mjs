// Modulos
//import { jwt } from "jsonwebtoken";
import { promisify } from 'util';
//
import pkg from 'jsonwebtoken';
const { sign } = pkg;
//
const jwtSecretProcessEnv = process.env.JWT_SECRET_KEY || '';
/**
 * Generates a JSON Web Token (JWT) for a given user email.
 * @param {Object} param - An object containing user information.
 * @param {string} param.userEmail - The email of the user for whom the token is generated.
 * @returns {string} The generated JWT, which expires in 1 hour.
 */
export const jwtGenerator = ({ userEmail }) => {
    return sign({ userEmail: userEmail }, jwtSecretProcessEnv, { expiresIn: '1h' });
}