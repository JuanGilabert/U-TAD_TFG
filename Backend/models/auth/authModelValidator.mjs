// Importamos las librerias.
import { z } from 'zod';
import { validEmailRegex } from '../../utils/export/GenericRegex.mjs';
// Definir los esquemas.
const userSchema = z.object({
    userName: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    userPassword: z.string({
        required_error: "La clave es requerida",
        invalid_type_error: "La clave debe ser un string"
    }),
    userEmail: z.string({
        required_error: "El email es requerido",
        invalid_type_error: "El email debe ser un string"
    }).regex(validEmailRegex, "El correo debe ser valido")
});
const loginSchema = z.object({
    userPassword: z.string({
        required_error: "La clave es requerida",
        invalid_type_error: "La clave debe ser un string"
    }),
    userEmail: z.string({
        required_error: "El email es requerido",
        invalid_type_error: "El email debe ser un string"
    }).regex(validEmailRegex, "El correo debe ser valido")
});
const logoutSchema = z.object({
    userJWT: z.string({
        required_error: "El JWT es requerido",
        invalid_type_error: "El JWT debe ser un string"
    })
})
// Definimos las funciones que validan los datos.
function validateNewUser(user) { return userSchema.safeParseAsync(user); }
function validateLoginUser(user) { return loginSchema.safeParseAsync(user); }
function validateLogoutUser(user) { return logoutSchema.safeParseAsync(user); }
// Exportamos las funciones.
export { validateNewUser, validateLoginUser, validateLogoutUser };