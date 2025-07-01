// Modulos externos
import { z } from 'zod';
// Modulos locales
import {
    MAX_STRING_TITLE_VALUE, MAX_STRING_DESCRIPTION_VALUE,
    MIN_FIELD_VALUE, MAX_ARRAY_VALUE, fechaISO8601Regex
} from '../../utils/export/GenericRegex.mjs';
// Definimos el esquema.
const workSchema = z.object({
    tituloTarea: z.string({
        required_error: "El titulo de la tarea es requerido",
        invalid_type_error: "El titulo de la tarea debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El titulo de la tarea no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El titulo de la tarea no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    descripcionTarea: z.string({
        required_error: "La descripcion de la tarea es requerida",
        invalid_type_error: "La descripcion de la tarea debe ser un texto"
    }).min(MIN_FIELD_VALUE, "La descripción de la tarea no puede estar vacía")
    .max(MAX_STRING_DESCRIPTION_VALUE, `La descripción de la tarea no puede tener más de ${MAX_STRING_DESCRIPTION_VALUE} caracteres.`),
    prioridadTarea: z.string({
        required_error: "La prioridad de la tarea es requerida",
        invalid_type_error: "La prioridad de la tarea debe ser un texto"
    }).min(MIN_FIELD_VALUE, "La prioridad de la tarea no puede estar vacía")
    .max(MAX_STRING_TITLE_VALUE, `La prioridad de la tarea no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    fechaInicioTarea: z.string({
        required_error: "La fecha de inicio de la tarea es requerida",
        invalid_type_error: "La fecha de inicio de la tarea debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de inicio de la tarea debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    fechaEntregaTarea: z.string({
        required_error: "La fecha de entrega de la tarea es requerida",
        invalid_type_error: "La fecha de entrega de la tarea debe ser un texto en formato ISO 8601"
    }).regex(fechaISO8601Regex, "La fecha de entrega de la tarea debe estar en formato ISO 8601(YYYY-MM-DDTHH:MM:SS o YYYY-MM-DDTHH:MM:SS.sss+HH:MM)")
    .transform((value) => {
        // Convertimos el string a un objeto Date
        const date = new Date(value);
        // Validamos si la conversión fue exitosa (si la fecha es válida)
        if (isNaN(date.getTime())) throw new Error("La fecha no es válida.");
        // Si la fecha es válida, la devolvemos.
        return date;
    }),
    organizadorTarea: z.string({
        required_error: "El organizador de la tarea es requerido",
        invalid_type_error: "El organizador de la tarea debe ser un texto"
    }).min(MIN_FIELD_VALUE, "El organizador de la tarea no puede estar vacío")
    .max(MAX_STRING_TITLE_VALUE, `El organizador de la tarea no puede tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`),
    colaboradoresTarea: z.array(
        z.string({
            required_error: "El nombre del colaborador de la tarea es requerido",
            invalid_type_error: "El nombre del colaborador de la tarea debe ser un texto"
        }).min(MIN_FIELD_VALUE, "El nombre del colaborador de la tarea no puede estar vacío")
        .max(50, "El nombre del colaborador de la tarea es demasiado largo")
    ).max(MAX_ARRAY_VALUE, `La lista de colaboradores no puede tener más de ${MAX_ARRAY_VALUE} colaboradores.`),
    notasTarea: z.string().max(MAX_STRING_TITLE_VALUE, `Las notas de la tarea no pueden tener más de ${MAX_STRING_TITLE_VALUE} caracteres.`).optional()
});
// Definimos las funciones que validan los datos
export function validateWork(work) { return workSchema.safeParseAsync(work); }
export function validatePartialWork(work) { return workSchema.partial().safeParseAsync(work); }