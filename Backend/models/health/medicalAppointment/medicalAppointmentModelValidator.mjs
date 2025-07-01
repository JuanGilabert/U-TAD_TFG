// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../../utils/export/GenericRegex.mjs';
const medicalAppointmentSchema = z.object({
    fechaCitaMedica: z.string({
        required_error: "La fecha de la cita medica es requerida",
        invalid_type_error: "La fecha de la cita medica debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de la cita medica debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de la cita medica no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    servicioCitaMedica: z.string({
        required_error: "El servicio de la cita medica es requerido",
        invalid_type_error: "El servicio de la cita medica debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El servicio de la cita medica no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El servicio de la cita medica no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    tipoPruebaCitaMedica: z.string({
        required_error: "El tipo de prueba de la cita medica es requerido",
        invalid_type_error: "El tipo de prueba de la cita medica debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El tipo de prueba de la cita medica no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El tipo de prueba de la la cita medica no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    lugarCitaMedica: z.string({
        required_error: "El lugar de la cita medica es requerido",
        invalid_type_error: "El lugar de la cita medica debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de la cita medica no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de la cita medica no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres`),
    notasCitaMedica: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas de la cita medica no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos
export function validateMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.safeParseAsync(medicalAppointment); }
export function validatePartialMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.partial().safeParseAsync(medicalAppointment); }