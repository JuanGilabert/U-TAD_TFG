/* Definimos la expresión regular para validar la fecha en los validadores de los modelos/schemas.
2025-06-20T00:00:00Z  ||  2025-06-20 00:00:00Z  ||  (2025-06-20T00:00:00.123456Z  ||  2025-04-05T12:34:56+02:00  ||  2025-04-05T12:34:56-02:00)
export const fechaISO8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d{3})?Z?$/ */
export const fechaISO8601Regex = /^(\d{4})-(\d{2})-(\d{2})[T\s](\d{2}):(\d{2}):(\d{2})(\.\d+)?(Z|([+-])(\d{2}):(\d{2}))$/;

/* Definimos la expresión regular siendo insensible a mayúsculas y minúsculas con el flag 'i' 
para validar el id recibido en los controladores. El id es un randomUUID (UUID v4 segun/en base a RFC 4122).
550e8400-e29b-41d4-a716-100000000001 || 7d8d3f76-ec3d-11ed-a05b-0242ac120003 */
export const randomUUIDv4Regex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

/* Definimos la expresion regular que valida si un token es valido o no.
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiOFJqMWNxZjMiLCJ1c2VybmFtZSI6IjZERHVPMlhnbm93WiIsImVtYWlsIjoiSGMzRW9AZXhhbXBsZS5jb20ifQ.cK3GZT4GJ4nWGe8z_w9HBo4PiVSVMSB-eK7w-Klez9I
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidTNZTHdLVkgiLCJ1c2VybmFtZSI6InV4QWYwRVltZGpheCIsImVtYWlsIjoiQmhMczlAZXhhbXBsZS5jb20ifQ.3CKjvnEItI1lI3ojFm_m1105TcI2UpAhuU8cmW_R8fc*/
//export const tokenRegex = /^[A-Za-z0-9-_=]+\.[A-Za-z0-9-_=]+\.?[A-Za-z0-9-_.+/=]$/;
export const tokenRegex = /^[A-Za-z0-9-_]+=*\.[A-Za-z0-9-_]+=*\.[A-Za-z0-9-_.+/=]+$/;