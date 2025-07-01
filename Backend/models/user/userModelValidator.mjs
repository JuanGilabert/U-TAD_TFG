// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    emailRegex, passwordRegex,
    MAX_STRING_TITLE_VALUE, MIN_FIELD_VALUE,
} from '../../utils/export/GenericRegex.mjs';
// Definir los esquemas.
const userchema = z.object({
    userName: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre de usuario debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre de usuario no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre de usuario no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    userPassword: z.string({
        required_error: "La contraseña de usuario es requerida",
        invalid_type_error: "La contraseña de usuario debe ser un texto"
    }).regex(passwordRegex,
        "La contraseña debe contener al menos: 2 mayúsculas y 2 minúsculas de la Aa-Zz, 2 números del 0 al 9 y 2 carácteres especiales(@$!%*?&)"
    ),
    userEmail: z.string({
        required_error: "El email de usuario es requerido",
        invalid_type_error: "El email de usuario debe ser un texto"
    }).regex(emailRegex, "El email de usuario debe ser valido. Ejemplo: Hc3Eo@example.com")
});
// Definimos las funciones que validan los datos.
export function validateUser(user) { return userchema.safeParseAsync(user); }
export function validatePartialUser(user) { return userchema.partial().safeParseAsync(user); }