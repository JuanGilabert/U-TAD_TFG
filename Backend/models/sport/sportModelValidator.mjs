import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const sportSchema = z.object({
    nombreActividad: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    fechaInicioActividad: z.string({
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