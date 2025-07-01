// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const foodSchema = z.object({
    nombreRestaurante: z.string({
        required_error: "El nombre del restaurante es requerido",
        invalid_type_error: "El nombre del restaurante debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre del restaurante no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre del restaurante no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    direccionRestaurante: z.string({
        required_error: "La direccion del restaurante es requerida",
        invalid_type_error: "La direccion del restaurante debe ser un texto"
    }).min(MIN_FIELD_VALUE, "La direccion del restaurante no puede estar vacía")
    .max(MAX_STRING_TITLE_VALUE, `La direccion del restaurante no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    tipoComida: z.string({
        required_error: "El tipo de comida del restaurante es requerido",
        invalid_type_error: "El tipo de comida del restaurante debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El tipo de comida del restaurante no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El tipo de comida del restaurante no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    fechaReserva: z.string({
        required_error: "La fecha de la reserva del restaurante es requerida",
        invalid_type_error: "La fecha de la reserva del restaurante debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de la reserva del restaurante debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de la reserva del restaurante no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    asistentesReserva: z.number({
        required_error: "La cantidad de asistentes al restaurante es requerida",
        invalid_type_error: "La cantidad de asistentes al restaurante debe ser un numero entero"
    }).int().positive("La cantidad de asistentes al restaurante debe ser mayor que 0")
    .max(50, `La cantidad máxima de asistentes al restaurante es de ${50} asistentes.`),
    notasReserva: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas de la reserva no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos.
export function validateFood(food) { return foodSchema.safeParseAsync(food); }
export function validatePartialFood(food) { return foodSchema.partial().safeParseAsync(food); }