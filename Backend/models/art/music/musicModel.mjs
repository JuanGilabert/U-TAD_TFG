// Modulos de node
import path from 'node:path';
import { promisify } from 'node:util';
import { randomUUID } from 'node:crypto';
import { execFile } from 'node:child_process';
import { fileURLToPath } from 'url';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MUSIC_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
// Constantes locales
const execFileAsync = promisify(execFile);
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
//// Exportamos la clase.
export class MusicModel {
    /** Funcion asincrona para obtener todas las musics de un usuario.
     * Al aplicar .toArray() obtenemos un Promise<T[]>, es decir una promesa con un array de musics.
     * Si no hay datos sera un array vacio(sin longitud).
     * El metodo.toArray() se utiliza para convertir un cursor en un array y puede provocar excepciones en tiempo de ejecucion. Se captura en el controlador.
     * Parametros:
     * @param {string} userId Id del usuario al que pertenecen las musics.
     * @returns {Array<MongoDocument> | false} Un array con las musics o false si no hay.
     */
    static async getAllMusics(userId, fechaInicioEvento, fechaFinEvento) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        /* Si no se pasa la fecha de inicio y de fin de la tarea indica que no hay query params
        y por lo tanto enviamos todos los documentos que coincidan con el filtro(userId). */
        if (fechaInicioEvento === "hasNoValue") {
            if (fechaFinEvento === "hasNoValue") {
                const musics = await db.collection(MUSIC_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
                return musics.length ? musics : null
            }
            // Si por lo que sea alguien envia solo la fecha de fin se devolvera false indicando un error
            // en los parametros de la peticion ya que no se ha enviado la fecha de inicio previamente.
            return false;
        }
        //
        const dayOfInitEventDate = new Date(fechaInicioEvento.split("T")[0]);//Convertimos el string ISO8601 a Date sin horas
        const endDateOfInitEventDay = new Date(dayOfInitEventDate);// Convertimos la Date a una Date con hora 00:00:00
        endDateOfInitEventDay.setUTCDate(endDateOfInitEventDay.getUTCDate() + 1);
        //
        const dayOfEndEventDate = new Date(fechaFinEvento.split("T")[0]);
        const endDateOfEndEventDay = new Date(dayOfEndEventDate);
        endDateOfEndEventDay.setUTCDate(endDateOfEndEventDay.getUTCDate() + 1);
        let musics = "";
        if (fechaFinEvento !== "hasNoValue") {
            if (fechaFinEvento !== "hasNoValue") {
                musics = await db.collection(MUSIC_COLLECTION_NAME).find(
                    {
                        userId: userId,
                        fechaInicioEvento: { $gte: dayOfInitEventDate, $lt: endDateOfInitEventDay },
                        fechaFinEvento: { $gte: dayOfEndEventDate, $lt: endDateOfEndEventDay }
                    },
                    { projection: { userId: 0 } }
                ).toArray();
            }
            musics = await db.collection(MUSIC_COLLECTION_NAME).find(
                { userId: userId, fechaInicioEvento: { $gte: dayOfInitEventDate, $lt: endDateOfInitEventDay } },
                { projection: { userId: 0 } }
            ).toArray();
        }
        //
        return musics.length ? musics : false;
    }
    /** Funcion asincrona para obtener una musica por su id.
     * Parametros:
     * @param {string} id Id de la musica a obtener.
     * @param {string} userId Id del usuario al que pertenece la musica.
     * @returns {MongoDocument} La musica encontrada o null si no se encuentra.
     */
    static async getMusicById(id, userId) {
        /* El metodo .findOne() nos devuelve el documento en caso de existir o null en caso de no existir el documento indicado.
        Tambien puede provocar excepciones en tiempo de ejecucion si el id es incorrecto. Se captura en el controlador. */
        const db = await connectDB();
        return await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    /** Funcion asincrona para obtener la lista de fechas de reserva no disponibles.
     * 
     * @param {string} userId Id del usuario ¿¿??
     * @returns 
     */
    static async getMusicUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        /* Obtenemos una lista de fechas de las fechas de los documentos
        donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22). */
        const unavailableDates = await db.collection(MUSIC_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioEvento", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 3 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene al menos 1 valor, devolvemos el error.
        return unavailableDates.length ?
        { dates: unavailableDates.map(document => document.fecha.toISOString()) } : { dates: [] };
    }
    /** Funcion asincrona para crear una nueva musica.
     * El campo acknowledged: boolean te dice si el servidor de MongoDB confirmó que recibió y procesó la operación.
     * Si acknowledged === true: el servidor confirmó que la operación se realizó. Si es true y falla lanza excepcion.
     * Si acknowledged === false: no hay confirmación, y no puedes confiar en que la operación se haya completado
     * como por ejemplo cuando creas uno nuevo con un id existente al violarse la restruiccion de clave unica.
     * El resto de valores como (insertedId) seran undefined si el acknowledged es false.
     * Parametros:
     * @param {Object} params - Los parametros de la musica a crear.
     * @param {Object} params.music - Los datos de la musica.
     * @param {string} userId Id del usuario al que pertenece la musica.
     * @returns {Promise<MongoDocument & { id: string }>} La musica creada con su id.
     */
    static async postMusic({ music, userId }) {
        const db = await connectDB();
        const newMusic = {
            ...music,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(MUSIC_COLLECTION_NAME).insertOne(newMusic);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newMusic };
        } catch (error) {
            console.error('Error en postMusic:', error);
            throw new Error(`No se pudo insertar la música en la base de datos: ${error}`);
        }
    }
    /** Funcion asincrona para actualizar una musica.
     * value: WithId<TSchema> | null;
     * lastErrorObject?: Document;
     * ok: 0 | 1;
     * Parametros:
     * @param {Object} params - Los parametros de la musica.
     * @param {string} id Id de la musica a actualizar.
     * @param {Object} params.music - Los datos de la musica.
     * @param {string} userId Id del usuario al que pertenece la musica.
     * @returns {Promise<MongoDocument | false>} El documento actualizado, o false si no se encontro ningun documento.
     */
    static async putMusic({ id, music, userId }){
        const db = await connectDB();
        const newMusic = {
            ...music,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MUSIC_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMusic, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la musica: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    /** Funcion asincrona para actualizar una musica.
     * value: WithId<TSchema> | null;
     * lastErrorObject?: Document;
     * ok: 0 | 1;
     * Parametros:
     * @param {Object} params - Los parametros de la musica.
     * @param {string} id Id de la musica a actualizar.
     * @param {Object} params.music - Los datos de la musica.
     * @param {string} userId Id del usuario al que pertenece la musica.
     * @returns {Promise<MongoDocument | false>} El documento actualizado, o false si no se encontro ningun documento.
     */
    static async patchMusic({ id, music, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MUSIC_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...music, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la musica: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    /** Funcion asincrona para eliminar una musica.
     * Parametros:
     * @param {string} id Id de la musica a eliminar.
     * @param {string} userId Id del usuario al que pertenece la musica.
     * @returns {Boolean} Devolvemos el resultado de la operacion. 
     */
    static async deleteMusic(id, userId) {
        const db = await connectDB();
        // Se devolvera el resultdo logico de la operacion comprobando si findOneAndDelete devuelve algo distinto de null.
        return (await db.collection(MUSIC_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
    /** Funcion asincrona para descargar un video de YouTube.
     * Parametros:
     * @param {Object} videoData Parametro con la url y el formato del video.
     * @param {string} videoData.url Url del video de YouTube.
     * @param {string} videoData.format Formato del video.
     * @returns Devolvemos el path exacto del archivo descargado.
     */
    static async postMusicVideoDownload({ videoData }) {
        // Desestructuramos el parametro videoData para obtener la url y el formato.
        const { url, format } = videoData;
        // Creamos la ruta del script Python.
        const scriptPath = path.join(__dirname, '../../../scripts/YouTubeVideoDownloader.py');
        // Ejecutamos el script Python con execFileAsync().
        try {
            // Capturamos la salida desde stdout.
            const { stdout, stderr } = await execFileAsync('python3', [scriptPath, url, format]);
            // Mostramos los errores que surgen debido a excepciones en el script Python.
            if (stderr) {
                console.error('Error desde el script Python:', stderr.trim());
                return { type: "Error", message: stderr };
            }
            // Si la salida empieza con SUCCESS: entonces implica que se ha descargado el video.
            const output = stdout.trim();
            if (output.startsWith('SUCCESS:')) {
                // El path exacto del archivo descargado.
                const filePath = output.replace('SUCCESS:', '').trim();
                return filePath;
            }
            // Si la salida empieza con WARNING: entonces implica que no se ha descargado el video por un motivo no controlado.
            if (output.startsWith('WARNING:')) {
                console.warn('Warning desde el script Python:', output);
                return { type: "Warning", message: output };
            }
            // Si la salida empieza con ERROR: entonces implica que no se ha descargado el video.
            if (output.startsWith('ERROR:')) {
                console.error('Error desde el script Python:', output);
                return { type: "Error", message: output };
            }
            // Mostramos un aviso/warning con la salida inesperada del script.
            console.warn('Warning inesperado desde el script Python:', output);
            return { type: "UnknownWarning", message: output };
        } catch (error) {
            console.error('Error ejecutando el script Python:', error);
            throw new Error(`No se pudo descargar el video de YouTube: ${error}`);
        }
    }
}