// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const travelSchema = z.object({
    nombreDestinoViaje: z.string({
        required_error: "El nombre  del destino del viaje es requerido",
        invalid_type_error: "El nombre del destino del viaje debe ser un string"
    }).min(MIN_FIELD_VALUE, "El nombre del destino del viaje no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre del destino no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    fechaSalidaViaje: z.string({
        required_error: "La fecha de salida del viaje es requerida",
        invalid_type_error: "La fecha de salida del viaje debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de salida debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de salida del viaje no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    lugarSalidaViaje: z.string({
        required_error: "El lugar de salida del viaje es requerido",
        invalid_type_error: "El lugar de salida del viaje debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de salida del viaje no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de salida no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    lugarDestinoViaje: z.string({
        required_error: "El lugar de destino del viaje es requerido",
        invalid_type_error: "El lugar de destino del viaje debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de destino del viaje no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de destino del viaje no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    fechaRegresoViaje: z.string({
        required_error: "La fecha de regreso del viaje es requerida",
        invalid_type_error: "La fecha de regreso del viaje debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de regreso debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de regreso del viaje no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    transporteViaje: z.string({
        required_error: "El transporte del viaje es requerido",
        invalid_type_error: "El transporte del viaje debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El transporte del viaje no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El transporte del viaje no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional(),
    acompañantesViaje: z.array(
        z.string({
            required_error: "Los acompañantes son requeridos",
            invalid_type_error: "Los acompañantes deben ser un texto"
        }).min(1, "El nombre del acompañante del viaje no puede estar vacío")
        .max(50, "El nombre del acompañante del viaje es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista de asistentes/acompañantes no puede tener más de ${MAX_ARRAY_VALUE} asistentes/acompañantes.`),
    notasViaje: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas  del viaje no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos
export function validateTravel(travel) { return travelSchema.safeParseAsync(travel); }
export function validatePartialTravel(travel) { return travelSchema.partial().safeParseAsync(travel); }