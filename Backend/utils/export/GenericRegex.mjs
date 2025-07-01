//// SECURITY
/* Definimos la expresión regular siendo insensible a mayúsculas y minúsculas con el flag 'i'.
para validar el id recibido en los controladores. El id es un randomUUID (UUID v4 segun/en base a RFC 4122).
550e8400-e29b-41d4-a716-100000000001 || 7d8d3f76-ec3d-11ed-a05b-0242ac120003 */
export const randomUUIDv4Regex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
/* Definimos la expresion regular que valida si un token es valido o no.
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiOFJqMWNxZjMiLCJ1c2VybmFtZSI6IjZERHVPMlhnbm93WiIsImVtYWlsIjoiSGMzRW9AZXhhbXBsZS5jb20ifQ
.cK3GZT4GJ4nWGe8z_w9HBo4PiVSVMSB-eK7w-Klez9I
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidTNZTHdLVkgiLCJ1c2VybmFtZSI6InV4QWYwRVltZGpheCIsImVtYWlsIjoiQmhMczlAZXhhbXBsZS5jb20ifQ
.3CKjvnEItI1lI3ojFm_m1105TcI2UpAhuU8cmW_R8fc */
//export const tokenRegex = /^[A-Za-z0-9-_=]+\.[A-Za-z0-9-_=]+\.?[A-Za-z0-9-_.+/=]$/; // SEGURAMENTE EN ALGUN CASO SERIA OPCIONAL UN BLOQUE. REVISAR LE ESTRUCTURA DEL JWT.
export const tokenRegex = /^[A-Za-z0-9-_]+=*\.[A-Za-z0-9-_]+=*\.[A-Za-z0-9-_.+/=]+$/;
//// Definimos el Regex para validar un email. Ejemplo valido de email que coincide con el regex: Hc3Eo@example.com
export const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
//// Definimos el Regex para validar un password.
// condición lookahead: (?=...) indica que en alguna parte de la expresion debe existir lo indicado.
//. = cualquier carácter, * = cero o más repeticiones, [a-z] = una letra minúscula, [A-Z] = una letra mayúscula,
// \d = un número(es como poner [0-9]), [@$!%*?&] = un carácter especial de esta lista exacta,
// [A-Za-z\d@$!%*?&]{8,} = indica que solo puede contener letras, números y símbolos válidos, y que su longitud mínima es de 8 caracteres.
export const passwordRegex = /^(?=(?:.*[a-z]){2})(?=(?:.*[A-Z]){2})(?=(?:.*\d){2})(?=(?:.*[@$!%*?&]){2})[A-Za-z\d@$!%*?&]{8,}$/;
/* Definimos la expresión regular para validar la fecha en los validadores de los modelos/schemas. 2025-06-20T12:00:00.000+00:00
Conviene recordar que los grupos de captura () en vez de los grupos no capturantes (?:) son necesarios aqui
ya que el valor ha de guardarse para luego operar con ellos. */
export const fechaISO8601Regex = /^(\d{4})-(\d{2})-(\d{2})[T\s](\d{2}):(\d{2}):(\d{2})(\.\d+)?([+-]\d{2}:\d{2})?$/;
//// Definimos el regex que valida la url de un video de youtube.
//export const youtubeVideoRegex = /^(https?://)?(www\.)?(youtube\.com|youtu\.be)/.+*$/;
export const youtubeVideoRegex = /^(https?:\/\/)?(www\.)?(youtube\.com|youtu\.be)\/(watch\?v=|embed\/|v\/)?$/;
//// Zod Validator Configuration
// Límite seguro para campos string normales como descripciones o notas de las tareas o eventos.
export const MAX_STRING_DESCRIPTION_VALUE = 2000;
// Límite seguro para campos string normales como nombres, titulos o lugares/direcciones de las tareas o eventos.
export const MAX_STRING_TITLE_VALUE = 200;
// Número máximo razonable de actores Numero maximo razonable de valores en los arrays.
export const MAX_ARRAY_VALUE = 25;
// Numero mínimo para cualquier campo que no sea una fecha.
export const MIN_FIELD_VALUE = 1;
// Numero maximo para la duracion de tareas o eventos. 24 horas como máximo.
export const MAX_DURATION_VALUE = 1440;
export const MAX_FLOAT_VALUE = 10000;        // Precio límite alto para entradas