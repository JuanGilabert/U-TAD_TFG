import { z } from 'zod';
import { fechaISO8601Regex, youtubeVideoRegex } from '../../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const musicSchema = z.object({
    nombreEvento: z.string({
        required_error: "El nombre es requerido",
        invalid_type_error: "El nombre debe ser un string"
    }),
    descripcionEvento: z.string({
        required_error: "La descripcion es requerida",
        invalid_type_error: "La descripcion debe ser un string"
    }),
    artistasEvento: z.array(z.string({
        required_error: "El artista es requerido",
        invalid_type_error: "El artista debe ser un string"
    })),
    fechaInicioEvento: z.string({
        required_error: "La fecha de inicio es requerida",
        invalid_type_error: "La fecha debe ser un string en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS.sss+HH:MM o YYYY-MM-DD HH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) {
            throw new Error("La fecha no es válida.");
        }
        return date;
    }),
    fechaFinEvento: z.string({
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
    lugarEvento: z.string({
        required_error: "El lugar es requerido",
        invalid_type_error: "El lugar debe ser un string"
    }),
    precioEvento: z.number({
        required_error: "El precio es requerido",
        invalid_type_error: "El precio debe ser un number"
    }),
    notasEvento: z.string().optional()
});
const musicVideoDownloadSchema = z.object({
    url: z.string({
        required_error: "La url es requerida",
        invalid_type_error: "La url debe ser un string"
    }).regex(youtubeVideoRegex, "La url debe ser de un video de youtube"),
    formato: z.enum(["aac", "aiff", "alac", "dsd", "flac", "pcm", "mp3", "mp4", "ogg", "wav"]).optional()//establecer los indicados en el script.PY
});
// Exportamos las funciones que validan los datos.
export function validateMusic(music) { return musicSchema.safeParseAsync(music); }
export function validatePartialMusic(music) { return musicSchema.partial().safeParseAsync(music); }
export function validateMusicVideoDownload(musicVideo) { return musicVideoDownloadSchema.safeParseAsync(musicVideo); }