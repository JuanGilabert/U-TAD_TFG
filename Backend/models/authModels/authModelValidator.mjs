import { z } from 'zod';
//
const validEmailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
// Definir el esquema
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
    }).regex(validEmailRegex, "El correo debe ser valido"),
    userJWT: z.string().optional()
});

function validateNewUser(cinema) {
    return userSchema.safeParseAsync(cinema);
}
function validatePartialNewUser(cinema) {
    return userSchema.partial().safeParseAsync(cinema);
}
//
export { validateNewUser, validatePartialNewUser }