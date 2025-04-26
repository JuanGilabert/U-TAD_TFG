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
    codigoNacionalMedicamento: z.string({
        required_error: "El codigo nacional es requerido",
        invalid_type_error: "El codigo nacional debe ser un string"
    }),
    nombreMedicamento: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    viaAdministracionMedicamento: z.object({
        forma: formaEnum,
        tipo: z.array(tipoEnum).length(1, "Debe contener exactamente un valor permitido")
    }),
    cantidadTotalCajaMedicamento: z.number({
        required_error: "La cantidad es requerida",
        invalid_type_error: "La cantidad debe ser un number entero"
    }).int().positive(),
    fechaCaducidadMedicamento: z.string({
        required_error: "La fecha de caducidad es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS.sssZ)"),
    notasMedicamento: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewMedicament(medicament) { return medicamentSchema.safeParseAsync(medicament); }
function validatePartialNewMedicament(medicament) { return medicamentSchema.partial().safeParseAsync(medicament); }
// Exportamos las funciones
export { validateNewMedicament, validatePartialNewMedicament }