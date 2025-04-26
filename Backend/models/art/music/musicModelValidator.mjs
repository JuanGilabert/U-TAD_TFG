import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const musicSchema = z.object({
    nombreEvento: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    descripcionEvento: z.string({
        required_error: "La descripcion es requerida",
        invalid_type_error: "La descripcion debe ser un string"
    }),
    artistasEvento: z.array(z.string({
        required_error: "El artista es requerido",
        invalid_type_error: "El artista debe ser un string"
    })),
    fechaInicioEvento: z.string({
        required_error: "La fecha de caducidad es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    fechaFinEvento: z.string({
        required_error: "La fecha de caducidad es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    lugarEvento: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    precioEvento: z.number({
        required_error: "El precio es requerido",
        invalid_type_error: "El precio debe ser un number"
    }),
    notasEvento: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewMusic(music) { return musicSchema.safeParseAsync(music); }
function validatePartialNewMusic(music) { return musicSchema.partial().safeParseAsync(music); }
// Exportamos las funciones
export { validateNewMusic, validatePartialNewMusic }