import { z } from 'zod';
import { fechaISO8601Regex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const cinemaSchema = z.object({
    nombrePelicula: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    descripcionPelicula: z.string({
        required_error: "La descripcion es requerida",
        invalid_type_error: "El nombre debe ser un string"
    }),
    actoresPelicula: z.array(z.string({
        required_error: "El actor es requerido",
        invalid_type_error: "El actor debe ser un string"
    })),
    fechaInicioPelicula: z.string({
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
    duracionPeliculaMinutos: z.number({
        required_error: "La duracion es requerida",
        invalid_type_error: "La duracion debe ser un number entero"
    }).int().positive(),
    lugarPelicula: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    precioEntradaPelicula: z.number({
        required_error: "El precio es requerido",
        invalid_type_error: "El precio debe ser un number"
    }),
    notasPelicula: z.string().optional()
});
// Definimos las funciones que validan los datos
function validateNewCinema(cinema) { return cinemaSchema.safeParseAsync(cinema); }
// Al ser partial, podemos omitir campos dado que todos se convierten en opcionales.
function validatePartialNewCinema(cinema) { return cinemaSchema.partial().safeParseAsync(cinema); }
// Exportamos las funciones
export { validateNewCinema, validatePartialNewCinema }