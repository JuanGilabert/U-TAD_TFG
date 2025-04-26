import { z } from 'zod';
import { fechaISO8601Regex } from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const travelSchema = z.object({
    nombreDestinoViaje: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    fechaSalidaViaje: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    fechaRegresoViaje: z.string({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un string"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    transporteViaje: z.string({
        required_error: "El transporte es requerido",
        invalid_type_error: "El transporte debe ser un string"
    }),
    lugarSalidaViaje: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    lugarDestinoViaje: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    acompañantesViaje: z.array(z.string({
        required_error: "El asistente es requerido",
        invalid_type_error: "El asistente debe ser un string"
    })).optional(),
    notasViaje: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewTravel(travel) { return travelSchema.safeParseAsync(travel); }
function validatePartialNewTravel(travel) { return travelSchema.partial().safeParseAsync(travel); }
// Exportamos las funciones
export { validateNewTravel, validatePartialNewTravel }