// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const meetingSchema = z.object({
    tituloReunion: z.string({
        required_error: "El titulo de la reunion es requerido",
        invalid_type_error: "El titulo de la reunion debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El titulo de la reunion no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El titulo de la reunion no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    tipoReunion: z.string({
        required_error: "El tipo de reunion es requerido",
        invalid_type_error: "El tipo de reunion debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El tipo de reunion no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El tipo de reunion no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    organizadorReunion: z.string({
        required_error: "El organizador de la reunion es requerido",
        invalid_type_error: "El organizador de la reunion debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El organizador de la reunion no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El organizador de la reunion no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    asistentesReunion: z.array(
        z.string({
            required_error: "Los asistentes de la reunion son requeridos",
            invalid_type_error: "Los asistentes de la reunion deben de ser un texto"
        }).min(MIN_FIELD_VALUE, "El nombre del asistente de la reunion no puede estar vacío")
        .max(50, "El nombre del asistente de la reunion es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista de asistentes a la reunion no puede tener más de ${MAX_ARRAY_VALUE} asistentes.`),
    fechaInicioReunion: z.string({
        required_error: "La fecha de inicio de la reunion es requerida",
        invalid_type_error: "La fecha de inicio de la reunion debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de inicio de la reunion debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de inicio de la reunion no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    fechaFinReunion: z.string({
        required_error: "La fecha de fin de la reunion es requerida",
        invalid_type_error: "La fecha de fin de la reunion debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de fin de la reunion debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de fin de la reunion no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    lugarReunion: z.string({
        required_error: "El lugar de la reunion es requerido",
        invalid_type_error: "El lugar de la reunion debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de la reunion no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de la reunion no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres`),
    notasReunion: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas de la reunion no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos.
export function validateMeeting(meeting) { return meetingSchema.safeParseAsync(meeting); }
export function validatePartialMeeting(meeting) { return meetingSchema.partial().safeParseAsync(meeting); }