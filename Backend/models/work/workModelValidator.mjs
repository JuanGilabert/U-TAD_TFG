import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const workSchema = z.object({
    tituloTarea: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    descripcionTarea: z.string({
        required_error: "El transporte es requerido",
        invalid_type_error: "El transporte debe ser un string"
    }),
    fechaInicioTarea: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    fechaEntregaTarea: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    prioridadTarea: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    notasTarea: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewWork(work) { return workSchema.safeParseAsync(work); }
function validatePartialNewWork(work) { return workSchema.partial().safeParseAsync(work); }
// Exportamos las funciones
export { validateNewWork, validatePartialNewWork }