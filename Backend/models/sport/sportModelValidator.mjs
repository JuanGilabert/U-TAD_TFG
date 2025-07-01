// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, MAX_DURATION_VALUE,
    fechaISO8601Regex
} from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const sportSchema = z.object({
    nombreActividad: z.string({
        required_error: "El nombre de la actividad es requerido",
        invalid_type_error: "El nombre de la actividad debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre de la actividad no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre de la actividad no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    fechaInicioActividad: z.string({
        required_error: "La fecha de inicio de la actividad es requerida",
        invalid_type_error: "La fecha de inicio de la actividad debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de la actividad debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    lugarActividad: z.string({
        required_error: "El lugar de la actividad es requerido",
        invalid_type_error: "El lugar de la actividad debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de la actividad no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de la actividad no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    duracionActividadMinutos: z.number({
        required_error: "La duracion de la actividad es requerida",
        invalid_type_error: "La duracion de la actividad debe ser un number entero"
    }).int().positive("La duración de la actividad debe ser mayor que 0").max(MAX_DURATION_VALUE, `La duración máxima es de ${MAX_DURATION_VALUE} minutos.`),
    asistentesActividad: z.array(
        z.string({
            required_error: "El asistente de la actividad es requerido",
            invalid_type_error: "El asistente de la actividad debe ser un texto"
        }).min(MIN_FIELD_VALUE, "El nombre del asistente de la actividad no puede estar vacío")
        .max(50, "El nombre del asistente de la actividad es demasiado largo.")
    ).max(MAX_ARRAY_VALUE, `La lista de asistentes de la actividad no puede tener más de ${MAX_ARRAY_VALUE} asistentes.`),
    notasActividad: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos
export function validateSport(sport) { return sportSchema.safeParseAsync(sport); }
export function validatePartialSport(sport) { return sportSchema.partial().safeParseAsync(sport); }