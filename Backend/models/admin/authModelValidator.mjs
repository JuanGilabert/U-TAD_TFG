// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    tokenRegex, emailRegex, passwordRegex
} from '../../utils/export/GenericRegex.mjs';
// Definir los esquemas.
const signInSchema = z.object({
    userPassword: z.string({
        required_error: "La contraseña de usuario es requerida",
        invalid_type_error: "La contraseña de usuario debe ser un texto"
    })/*.regex(passwordRegex,
        "La contraseña debe contener al menos: 2 mayúsculas y 2 minúsculas de la Aa-Zz, 2 números del 0 al 9 y 2 carácteres especiales(@$!%*?&)"
    )*/,
    userEmail: z.string({
        required_error: "El email de usuario es requerido",
        invalid_type_error: "El email de usuario debe ser un texto"
    }).regex(emailRegex, "El email de usuario debe ser valido. Ejemplo: Hc3Eo@example.com")
});
const signOutSchema = z.object({
    userJWT: z.string({
        required_error: "El token es requerido",
        invalid_type_error: "El token debe ser un texto"
    }).regex(tokenRegex, "El token tiene un formato incorrecto.")
})
// Definimos las funciones que validan los datos.
export function validateSignInUser(user) { return signInSchema.safeParseAsync(user); }
export function validateSignOutUser(user) { return signOutSchema.safeParseAsync(user); }