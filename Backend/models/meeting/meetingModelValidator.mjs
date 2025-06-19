import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definir el esquema
const meetingSchema = z.object({
    tituloReunion: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    tipoReunion: z.string({
        required_error: "El tipo es requerido",
        invalid_type_error: "El tipo debe ser un string"
    }),
    organizadorReunion: z.string({
        required_error: "El organizador es requerido",
        invalid_type_error: "El organizador debe ser un string"
    }),
    asistentesReunion: z.array(z.string({
        required_error: "El asistente es requerido",
        invalid_type_error: "El asistente debe ser un string"
    })),
    fechaInicioReunion: z.string({
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
    fechaFinReunion: z.string({
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
    lugarReunion: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    notasReunion: z.string().optional()
});
// Exportamos las funciones que validan los datos.
export function validateMeeting(meeting) { return meetingSchema.safeParseAsync(meeting); }
export function validatePartialMeeting(meeting) { return meetingSchema.partial().safeParseAsync(meeting); }