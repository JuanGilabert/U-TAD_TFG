// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../../utils/export/GenericRegex.mjs';
// Definir los valores permitidos para "forma" y "tipo".
const formaEnum = z.enum([
    "oral", "inhalacion", "topica", "oftalmologica", "otica", "nasal", "rectal", "vaginal", "parenteral", "inyeccion"
]);
const tipoEnum = z.enum([
    "comprimidos", "pastillas", "tabletas", "capsulas", "jarabe", "solucion", "granulado", "polvos",
    "pastillas sublinguales", "pastillas bucales", "aerosol", "inhalador", "nebulizador", "crema", "pomada",
    "gel", "locion", "parches", "colirio", "gotas", "supositorios", "enemas", "ovulos", "cremas", "geles",
    "intravenosa", "intramuscular", "subcutanea", "intradermica"
]);
// Definimos el esquema
const medicamentSchema = z.object({
    nombreMedicamento: z.string({
        required_error: "El nombre del medicamento es requerido",
        invalid_type_error: "El nombre del medicamento debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre del medicamento no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre del medicamento no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    viaAdministracionMedicamento: z.object({
        forma: formaEnum,
        tipo: tipoEnum
    }),
    cantidadTotalCajaMedicamento: z.number({
        required_error: "La cantidad de la caja del medicamento es requerida",
        invalid_type_error: "La cantidad de la caja del medicamento debe ser un number entero"
    }).int().positive("La cantidad de la caja del medicamento debe ser mayor que 0")
    .max(50, `La cantidad máxima de la caja del medicamento es de ${50}.`),
    fechaCaducidadMedicamento: z.string({
        required_error: "La fecha de caducidad del medicamento es requerida",
        invalid_type_error: "La fecha de caducidad del medicamento debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de caducidad del medicamento debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de caducidad del medicamento no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    notasMedicamento: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas del medicamento no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Exportamos las funciones que validan los datos.
export function validateMedicament(medicament) { return medicamentSchema.safeParseAsync(medicament); }
export function validatePartialMedicament(medicament) { return medicamentSchema.partial().safeParseAsync(medicament); }