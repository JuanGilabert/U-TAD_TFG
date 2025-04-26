import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definir el esquema
const meetingSchema = z.object({
    tituloReunion: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    tipoReunion: z.string({
        required_error: "El tipo es requerida",
        invalid_type_error: "El nombre debe ser un string"
    }),
    organizadorReunion: z.string({
        required_error: "El tipo es requerida",
        invalid_type_error: "El tipo debe ser un string"
    }),
    asistentesReunion: z.array(z.string({
        required_error: "El asistente es requerido",
        invalid_type_error: "El asistente debe ser un string"
    })),
    fechaInicioReunion: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    lugarReunion: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    notasReunion: z.string().optional()
});
// Definir las funciones que validan los datos
function validateNewMeeting(meeting) { return meetingSchema.safeParseAsync(meeting); }
function validatePartialNewMeeting(meeting) { return meetingSchema.partial().safeParseAsync(meeting); }
// Exportar las funciones
export { validateNewMeeting, validatePartialNewMeeting }