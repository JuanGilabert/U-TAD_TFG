// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const paintingSchema = z.object({
    nombreExposicionArte: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres`),
    descripcionExposicionArte: z.string({
        required_error: "La descripcion es requerida",
        invalid_type_error: "La descripcion debe ser texto"
    }).min(MIN_FIELD_VALUE, "La descripción no puede estar vacía")
    .max(MAX_STRING_DESCRIPTION_VALUE, `La descripción no puede tener más de ${MAX_STRING_DESCRIPTION_VALUE} caracteres`),
    pintoresExposicionArte: z.array(
        z.string({
            required_error: "Los pintores son requeridos",
            invalid_type_error: "El pintor debe ser un texto"
        }).min(1, "El nombre del pintor no puede estar vacío").max(50, "El nombre del pintor es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista no puede tener más de ${MAX_ARRAY_VALUE} elementos`),
    fechaInicioExposicionArte: z.string({
        required_error: "La fecha de inicio es requerida",
        invalid_type_error: "La fecha debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    fechaFinExposicionArte: z.string({
        required_error: "La fecha de inicio es requerida",
        invalid_type_error: "La fecha debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    lugarExposicionArte: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres`),
    precioEntradaExposicionArte: z.number({
        required_error: "El precio es requerido",
        invalid_type_error: "El precio debe ser un number"
    }).gt(0, { message: "El precio debe ser mayor que 0" })
    .refine(n => !Number.isInteger(n), { message: "El precio debe ser un número decimal (no entero)" }),
    notasExposicionArte: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres`).optional()
});
// Exportamos las funciones que validan los datos.
export function validatePainting(painting) { return paintingSchema.safeParseAsync(painting); }
export function validatePartialPainting(painting) { return paintingSchema.partial().safeParseAsync(painting); }