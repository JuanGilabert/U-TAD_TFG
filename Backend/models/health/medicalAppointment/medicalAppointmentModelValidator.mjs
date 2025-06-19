import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const medicalAppointmentSchema = z.object({
    fechaCitaMedica: z.string({
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
    servicioCitaMedica: z.string({
        required_error: "El servicio es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    tipoPruebaCitaMedica: z.string({
        required_error: "El tipo de prueba es requerido",
        invalid_type_error: "El tipo de prueba debe ser un string"
    }),
    lugarCitaMedica: z.string({
        required_error: "El lugar de la cita es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    notasCitaMedica: z.string().optional()
});
// Exportamos las funciones que validan los datos
export function validateMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.safeParseAsync(medicalAppointment); }
export function validatePartialMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.partial().safeParseAsync(medicalAppointment); }