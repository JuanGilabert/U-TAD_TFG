import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
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
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    viaAdministracionMedicamento: z.object({
        forma: formaEnum,
        tipo: tipoEnum
    }),
    cantidadTotalCajaMedicamento: z.number({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un number entero"
    }).int().positive(),
    fechaCaducidadMedicamento: z.string({
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
    notasMedicamento: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewMedicament(medicament) { return medicamentSchema.safeParseAsync(medicament); }
function validatePartialNewMedicament(medicament) { return medicamentSchema.partial().safeParseAsync(medicament); }
// Exportamos las funciones
export { validateNewMedicament, validatePartialNewMedicament }