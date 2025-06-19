// Importamos las librerias.
import { z } from 'zod';
import { emailRegex, tokenRegex } from '../../utils/export/GenericRegex.mjs';
// Definir los esquemas.
const signInSchema = z.object({
    userPassword: z.string({
        required_error: "La clave es requerida",
        invalid_type_error: "La clave debe ser un string"
    }),
    userEmail: z.string({
        required_error: "El email es requerido",
        invalid_type_error: "El email debe ser un string"
    }).regex(emailRegex, "El correo debe ser valido")
});
const signOutSchema = z.object({
    userJWT: z.string({
        required_error: "El JWT es requerido",
        invalid_type_error: "El JWT debe ser un string"
    }).regex(tokenRegex, "El JWT debe ser valido")
})
// Definimos las funciones que validan los datos.
export function validateSignInUser(user) { return signInSchema.safeParseAsync(user); }
export function validateSignOutUser(user) { return signOutSchema.safeParseAsync(user); }