// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, MAX_DURATION_VALUE,
    fechaISO8601Regex
} from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const cinemaSchema = z.object({
    nombrePelicula: z.string({
        required_error: "El nombre de la pelicula es requerido",
        invalid_type_error: "El nombre de la pelicula debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre de la pelicula no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre de la pelicula no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    descripcionPelicula: z.string({
        required_error: "La descripcion/sinopsis de la pelicula es requerida",
        invalid_type_error: "La descripcion/sinopsis de la pelicula debe ser un texto"
    }).min(1, "La descripción/sinopsis de la pelicula no puede estar vacía")
    .max(MAX_STRING_DESCRIPTION_VALUE, `La descripción/sinopsis de la pelicula no puede tener más de ${MAX_STRING_DESCRIPTION_VALUE} caracteres.`),
    actoresPelicula: z.array(
        z.string({
            required_error: "El nombre del actor de la pelicula es requerido",
            invalid_type_error: "El nombre del actor de la pelicula debe ser un texto"
        }).min(MIN_FIELD_VALUE, "El nombre del actor de la pelicula no puede estar vacío")
        .max(50, "El nombre del actor de la pelicula es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista de actores no puede tener más de ${MAX_ARRAY_VALUE} actores.`),
    fechaInicioPelicula: z.string({
        required_error: "La fecha de inicio de la pelicula es requerida",
        invalid_type_error: "La fecha de inicio de la pelicula debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de inicio de la pelicula debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        return date;
    }),
    duracionPeliculaMinutos: z.number({
        required_error: "La duracion de la pelicula es requerida",
        invalid_type_error: "La duracion de la pelicula debe ser un number"
    }).int().positive("La duración de la pelicula debe ser mayor que 0")
    .max(MAX_DURATION_VALUE, `La duración máxima de la pelicula es de ${MAX_DURATION_VALUE} minutos.`),
    lugarPelicula: z.string({
        required_error: "El lugar de la pelicula es requerido",
        invalid_type_error: "El lugar de la pelicula debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar de la pelicula no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar de la pelicula no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    precioEntradaPelicula: z.number({
        required_error: "El precio de la entrada de la pelicula es requerido",
        invalid_type_error: "El precio de la entrada de la pelicula debe ser un number"
    }).gt(0, { message: "El precio de la entrada de la pelicula debe ser mayor que 0" })
    .refine(n => !Number.isInteger(n), {
        message: "El precio de la entrada de la pelicula debe ser un número decimal (no entero)"
    }),
    notasPelicula: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas de la pelicula no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres`).optional()
});
// Exportamos las funciones que validan los datos
export function validateCinema(cinema) { return cinemaSchema.safeParseAsync(cinema); }
// Al ser partial, podemos omitir campos dado que todos se convierten en opcionales.
export function validatePartialCinema(cinema) { return cinemaSchema.partial().safeParseAsync(cinema); }