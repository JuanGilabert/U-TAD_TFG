// Modulos
import { verify } from "jsonwebtoken";
////
export const jwtValidator = (token) => verify(token, process.env.JWT_SECRET, { algorithms: ['HS256'] })
    .then(({ userEmail }) => userEmail)// Devolvemos la propiedad creada en el payload del token(userEmail) cuando se genera.
    .catch(() => null);