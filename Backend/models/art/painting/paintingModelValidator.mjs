import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const paintingSchema = z.object({
    nombreExposicionArte: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    descripcionExposicionArte: z.string({
        required_error: "La descripcion es requerida",
        invalid_type_error: "La descripcion debe ser un string"
    }),
    pintoresExposicionArte: z.array(z.string({
        required_error: "Los pintores son requeridos",
        invalid_type_error: "El artista debe ser un string"
    })),
    fechaInicioExposicionArte: z.string({
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
    fechaFinExposicionArte: z.string({
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
    lugarExposicionArte: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    precioEntradaExposicionArte: z.number({
        required_error: "El precio es requerido",
        invalid_type_error: "El precio debe ser un number"
    }),
    notasExposicionArte: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewPainting(painting) { return paintingSchema.safeParseAsync(painting); }
function validatePartialNewPainting(painting) { return paintingSchema.partial().safeParseAsync(painting); }
// Exportamos las funciones.
export { validateNewPainting, validatePartialNewPainting }