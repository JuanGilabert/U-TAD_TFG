import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const foodSchema = z.object({
    nombreRestaurante: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    direccionRestaurante: z.string({
        required_error: "La direccion es requerida",
        invalid_type_error: "El nombre debe ser un string"
    }),
    tipoComida: z.string({
        required_error: "El tipo de comida es requerido",
        invalid_type_error: "El tipo debe ser un string"
    }),
    fechaReserva: z.string({
        required_error: "La fecha de inicio es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) {
            throw new Error("La fecha no es válida.");
        }
        return date;
    }),
    asistentesReserva: z.number({
        required_error: "La cantidad de asistentes es requerido",
        invalid_type_error: "La cantidad debe ser un numero entero"
    }).int().positive(),
    notasReserva: z.string().optional()
});
// Exportamos las funciones que validan los datos.
export function validateFood(food) { return foodSchema.safeParseAsync(food); }
export function validatePartialFood(food) { return foodSchema.partial().safeParseAsync(food); }