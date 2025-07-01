// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE,
    fechaISO8601Regex, youtubeVideoRegex
} from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema
const musicSchema = z.object({
    nombreEvento: z.string({
        required_error: "El nombre del evento es requerido",
        invalid_type_error: "El nombre del evento debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El nombre del evento no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El nombre del evento no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    descripcionEvento: z.string({
        required_error: "La descripcion del evento es requerida",
        invalid_type_error: "La descripcion del evento debe ser un texto"
    }).min(MIN_FIELD_VALUE, "La descripción del evento no puede estar vacía")
    .max(MAX_STRING_DESCRIPTION_VALUE, `La descripción del evento no puede tener más de ${MAX_STRING_DESCRIPTION_VALUE} caracteres.`),
    artistasEvento: z.array(
        z.string({
            required_error: "El nombre del artista del evento es requerido",
            invalid_type_error: "El nombre del artista del evento debe ser un string"
        }).min(MIN_FIELD_VALUE, "El nombre del artista del evento no puede estar vacío")
        .max(50, "El nombre del artista del evento es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista de artistas del evento no puede tener más de ${MAX_ARRAY_VALUE} artistas.`),
    fechaInicioEvento: z.string({
        required_error: "La fecha de inicio del evento es requerida",
        invalid_type_error: "La fecha de inicio del evento debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de inicio del evento debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS.sss+HH:MM o YYYY-MM-DD HH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de inicio del evento no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    fechaFinEvento: z.string({
        required_error: "La fecha de fin del evento es requerida",
        invalid_type_error: "La fecha de fin del evento debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de fin del evento debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha de fin del evento no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    lugarEvento: z.string({
        required_error: "El lugar del evento es requerido",
        invalid_type_error: "El lugar del evento debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El lugar del evento no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El lugar del evento no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    precioEvento: z.number({
        required_error: "El precio del evento es requerido",
        invalid_type_error: "El precio del evento debe ser un number"
    }).gt(0, { message: "El precio del evento debe ser mayor que 0" })
    .refine(n => !Number.isInteger(n), {
        message: "El precio del evento debe ser un número decimal (no entero)"
    }),
    notasEvento: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas del evento no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
const musicVideoDownloadSchema = z.object({
    url: z.string({
        required_error: "La url del video de YouTube es requerida",
        invalid_type_error: "La url del video de YouTube debe ser un texto"
    }).regex(youtubeVideoRegex, "La url del video de YouTube debe de tener el formato correcto:\
        https://www.youtube.com/watch?v=VIDEO_ID o https://www.youtu.be/").optional(),
    formato: z.enum(["aac", "aiff", "alac", "flac", "wav", "ogg", "dsd", "pcm", "mp3", "mp4"])
});
// Exportamos las funciones que validan los datos.
export function validateMusic(music) { return musicSchema.safeParseAsync(music); }
export function validatePartialMusic(music) { return musicSchema.partial().safeParseAsync(music); }
export function validateMusicVideoDownload(musicVideo) { return musicVideoDownloadSchema.safeParseAsync(musicVideo); }