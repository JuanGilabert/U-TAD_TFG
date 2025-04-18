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
        required_error: "El tipo es requerida",
        invalid_type_error: "El tipo debe ser un string"
    }),
    fechaReserva: z.string({
        required_error: "La fecha de caducidad es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    horaReserva: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }),
    asistentesReserva: z.number({
        required_error: "La cantidad de asistentes es requerida",
        invalid_type_error: "La cantidad debe ser un numero entero"
    }).int().positive(),
    notasReserva: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewFood(food) { return foodSchema.safeParseAsync(food); }
function validatePartialNewFood(food) { return foodSchema.partial().safeParseAsync(food); }
// Exportamos las funciones
export { validateNewFood, validatePartialNewFood }