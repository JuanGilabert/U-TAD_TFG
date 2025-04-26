import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const sportSchema = z.object({
    nombreActividad: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    fechaInicioActividad: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    lugarActividad: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    duracionActividadMinutos: z.number({
        required_error: "La duracion es requerida",
        invalid_type_error: "La duracion debe ser un number entero"
    }).int().positive(),
    asistentesActividad: z.array(z.string({
        required_error: "Los asistentes son requeridos",
        invalid_type_error: "El asistente debe ser un string"
    })),
    notasActividad: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewSport(sport) { return sportSchema.safeParseAsync(sport); }
function validatePartialNewSport(sport) { return sportSchema.partial().safeParseAsync(sport); }
// Exportamos las funciones
export { validateNewSport, validatePartialNewSport }