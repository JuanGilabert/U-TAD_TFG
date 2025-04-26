import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const medicalAppointmentSchema = z.object({
    fechaCitaMedica: z.string({
        required_error: "La fecha de caducidad es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    servicioCitaMedica: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    tipoPruebaCitaMedica: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "La cantidad debe ser un number entero"
    }),
    lugarCitaMedica: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    notasCitaMedica: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.safeParseAsync(medicalAppointment); }
function validatePartialNewMedicalAppointment(medicalAppointment) { return medicalAppointmentSchema.partial().safeParseAsync(medicalAppointment); }
// Exportamos las funciones
export { validateNewMedicalAppointment, validatePartialNewMedicalAppointment }