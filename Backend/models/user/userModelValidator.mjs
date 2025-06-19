// Importamos las librerias.
import { z } from 'zod';
import { passwordRegex, emailRegex } from '../../utils/export/GenericRegex.mjs';
// Definir los esquemas.
const userchema = z.object({
    userName: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    userPassword: z.string({
        required_error: "La clave es requerida",
        invalid_type_error: "La contraseña debe ser un string"
    }).regex(passwordRegex, "La contraseña debe ser valida"),
    userEmail: z.string({
        required_error: "El email es requerido",
        invalid_type_error: "El email debe ser un string"
    }).regex(emailRegex, "El correo debe ser valido")
});
// Definimos las funciones que validan los datos.
export function validateUser(user) { return userchema.safeParseAsync(user); }
export function validatePartialUser(user) { return userchema.partial().safeParseAsync(user); }